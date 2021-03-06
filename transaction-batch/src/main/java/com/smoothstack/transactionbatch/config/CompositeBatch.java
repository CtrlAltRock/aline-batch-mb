package com.smoothstack.transactionbatch.config;

import java.util.ArrayList;
import java.util.List;

import com.smoothstack.transactionbatch.mapper.CustomFieldSetMapper;
import com.smoothstack.transactionbatch.model.TransactRead;
import com.smoothstack.transactionbatch.tasklet.NullTasklet;
import com.smoothstack.transactionbatch.tasklet.ReportTasklet;
import com.smoothstack.transactionbatch.writer.enrich.CardsWriter;
import com.smoothstack.transactionbatch.writer.enrich.MerchantsWriter;
import com.smoothstack.transactionbatch.writer.enrich.UsersWriter;
import com.smoothstack.transactionbatch.writer.reports.ReportWriter;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;

@EnableBatchProcessing
@Configuration
public class CompositeBatch {
    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private TaskExecutor taskExecutor;

    @Bean
    @StepScope
    public FlatFileItemReader<TransactRead> transactionReader(
        @Value("#{jobParameters['inputFile']}")
        FileSystemResource inputFile
    ) {
        return new FlatFileItemReaderBuilder<TransactRead>()
                .linesToSkip(1)
                .name("csvflatfileitemreader")
                .resource(inputFile)
                .delimited()
                .names(new String[]{"user", "card", "year", "month", "day", "time", "amount", "use", "merchant", "city", "state", "zip", "mcc", "errors", "isFraud"})
                .fieldSetMapper(new CustomFieldSetMapper())
                .build();
    }

    @Bean
    @StepScope
    public CompositeItemWriter<TransactRead> compositeItemWriter(
        @Value("#{jobParameters['enrich']}") String enrich,
        @Value("#{jobParameters['analyze']}") String analyze
    ) {
        CompositeItemWriter<TransactRead> writer = new CompositeItemWriter<>();

        List<ItemWriter<? super TransactRead>> writers = new ArrayList<>();

        if (enrich != null && !enrich.equals("false")) {
            writers.add(new UsersWriter());
            writers.add(new CardsWriter());
            writers.add(new MerchantsWriter());
        }

        if (analyze != null && !analyze.equals("false")) {
            writers.add(new ReportWriter());
        }

        writer.setDelegates(writers);

        return writer;
    }

    @Bean
    @StepScope
    public Tasklet configureReportTasklet(
        @Value("#{jobParameters['analyze']}") String analyze
    ) {
        if (analyze == null || analyze.equals("false")) return new NullTasklet();

        return new ReportTasklet();
    }

    @Bean
    public Step compositeStep() {
        return steps.get("Composite Step")
            .<TransactRead, TransactRead>chunk(5000)
            .reader(transactionReader( null ))
            .writer(compositeItemWriter(null, null))
            .allowStartIfComplete(true)
            .taskExecutor(taskExecutor)
            .build();
    }


    @Bean
    public Step writerReportStep() {
        return steps.get("Report Writer Step")
            .tasklet(configureReportTasklet( null ))
            .build();
    }
    
    @Bean
    public Job compositeJob() {
        return jobs.get("Composite Batch")
            .incrementer(new RunIdIncrementer())
            .start(compositeStep())
            .next(writerReportStep())
            .build();
    }
}