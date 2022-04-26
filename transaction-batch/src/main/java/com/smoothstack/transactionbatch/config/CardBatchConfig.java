package com.smoothstack.transactionbatch.config;

import java.util.HashMap;

import com.smoothstack.transactionbatch.mapper.CustomFieldSetMapper;
import com.smoothstack.transactionbatch.model.CardBase;
import com.smoothstack.transactionbatch.model.TransactRead;
import com.smoothstack.transactionbatch.processor.CardProcessor;

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

//@EnableBatchProcessing
//@Configuration
public class CardBatchConfig {
    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Bean
    @StepScope
    public FlatFileItemReader<TransactRead> transactionReader() {
        FileSystemResource inputFile = new FileSystemResource("input/card_transaction.v1.csv");
        
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
    public XStreamMarshaller cardXmlMarshaller() {
        HashMap<String, Class<CardBase>> alias = new HashMap<>();
        alias.put("card", CardBase.class);

        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setAliases(alias);

        return marshaller;
    }

    @Bean
    @StepScope
    public StaxEventItemWriter<CardBase> cardXmlWriter() {
        FileSystemResource output = new FileSystemResource("output/GeneratedCards.xml");

        return new StaxEventItemWriterBuilder<CardBase>()
            .name("cardXmlWriter")
            .resource(output)
            .marshaller(cardXmlMarshaller())
            .rootTagName("GeneratedCards")
            .build();
    }

    @Bean
    public Step cardStep() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(8);
        threadPoolTaskExecutor.setMaxPoolSize(500);
        threadPoolTaskExecutor.afterPropertiesSet();

        return steps.get("Card Process Step")
            .<TransactRead, CardBase>chunk(100000)
            .reader(transactionReader())
            .processor( new CardProcessor() )
            .writer(cardXmlWriter())
            .allowStartIfComplete(true)
            .taskExecutor(threadPoolTaskExecutor)
            .build();
    }
    
    @Bean
    public Job cardJob() {
        return jobs.get("Card Process")
            .incrementer(new RunIdIncrementer())
            .start(cardStep())
            .build();
    }
}
