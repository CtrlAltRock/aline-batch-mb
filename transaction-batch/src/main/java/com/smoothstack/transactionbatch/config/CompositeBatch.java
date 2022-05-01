package com.smoothstack.transactionbatch.config;

import java.util.Arrays;

import com.smoothstack.transactionbatch.mapper.CustomFieldSetMapper;
import com.smoothstack.transactionbatch.model.TransactRead;
import com.smoothstack.transactionbatch.processor.CardProcessor;
import com.smoothstack.transactionbatch.processor.MerchantProcessor;
import com.smoothstack.transactionbatch.processor.UserProcessor;
import com.smoothstack.transactionbatch.tasklet.MainWriter;
import com.smoothstack.transactionbatch.writer.ConsoleItemWriter;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
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
    public CompositeItemProcessor<TransactRead, TransactRead> compositeProcessor(
        @Value("#{jobParameters['enrich']}") String enrich
    ) {
        CompositeItemProcessor<TransactRead, TransactRead> processor = new CompositeItemProcessor<>();

        if (enrich != null && !enrich.equals("false")) {
            processor.setDelegates(Arrays.asList(new UserProcessor(), new CardProcessor(), new MerchantProcessor()));
        }

        return processor;
    }

    @Bean
    public Step compositeStep() {
        return steps.get("Composite Step")
            .<TransactRead, TransactRead>chunk(1000)
            .reader(transactionReader( null ))
            .processor(compositeProcessor(null))
            .writer(new ConsoleItemWriter())
            .allowStartIfComplete(true)
            .taskExecutor(taskExecutor)
            .build();
    }

    @Bean
    public Step writerStep() {
        return steps.get("Writer Step")
            .tasklet(new MainWriter())
            .build();
    }
    
    @Bean
    public Job compositeJob() {
        return jobs.get("Composite Batch")
            .incrementer(new RunIdIncrementer())
            .start(compositeStep())
            .next(writerStep())
            .build();
    }
}
