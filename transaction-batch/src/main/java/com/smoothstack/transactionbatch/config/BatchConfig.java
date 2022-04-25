package com.smoothstack.transactionbatch.config;

import java.util.HashMap;

import com.smoothstack.transactionbatch.mapper.CustomFieldSetMapper;
import com.smoothstack.transactionbatch.model.TransactRead;
import com.smoothstack.transactionbatch.model.UserBase;
import com.smoothstack.transactionbatch.processor.UserProcessor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableBatchProcessing
@Configuration
public class BatchConfig {
    
    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Bean
    @StepScope
    public FlatFileItemReader<TransactRead> transactionReader() {
        FileSystemResource inputFile = new FileSystemResource("input/test2.csv");
        
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
    public XStreamMarshaller userXmlMarshaller() {
        HashMap<String, Class<UserBase>> alias = new HashMap<>();
        alias.put("user", UserBase.class);

        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setAliases(alias);

        return marshaller;
    }

    @Bean
    @StepScope
    public StaxEventItemWriter<UserBase> userXmlWriter() {
        FileSystemResource output = new FileSystemResource("output/GeneratedUsers.xml");

        return new StaxEventItemWriterBuilder<UserBase>()
            .name("userXmlWriter")
            .resource(output)
            .marshaller(userXmlMarshaller())
            .rootTagName("GeneratedUsers")
            .build();
    }

    @Bean
    public Step threadedStep() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(8);
        threadPoolTaskExecutor.setMaxPoolSize(500);
        threadPoolTaskExecutor.afterPropertiesSet();

        return steps.get("Card Process Step")
            .<TransactRead, UserBase>chunk(10)
            .reader(transactionReader())
            .processor( new UserProcessor() )
            .writer(userXmlWriter())
            .taskExecutor(threadPoolTaskExecutor)
            .build();
    }
    
    @Bean
    public Job cardJob() {
        return jobs.get("Card Process")
            .incrementer(new RunIdIncrementer())
            .start(threadedStep())
            .build();
    }
}