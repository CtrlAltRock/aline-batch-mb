package com.smoothstack.transactionbatch.config;

import java.util.HashMap;

import com.smoothstack.transactionbatch.mapper.CustomFieldSetMapper;
import com.smoothstack.transactionbatch.model.MerchantBase;
import com.smoothstack.transactionbatch.model.TransactRead;
import com.smoothstack.transactionbatch.processor.MerchantProcessor;
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
public class MerchantBatchConfig {
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
    public XStreamMarshaller merchantXmlMarshaller() {
        HashMap<String, Class<MerchantBase>> alias = new HashMap<>();
        alias.put("merchant", MerchantBase.class);

        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setAliases(alias);

        return marshaller;
    }

    @Bean
    @StepScope
    public StaxEventItemWriter<MerchantBase> merchantXmlWriter() {
        FileSystemResource output = new FileSystemResource("output/GeneratedMerchants.xml");

        return new StaxEventItemWriterBuilder<MerchantBase>()
            .name("merchantXmlWriter")
            .resource(output)
            .marshaller(merchantXmlMarshaller())
            .rootTagName("GeneratedMerchants")
            .build();
    }

    @Bean
    public Step merchantStep() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(8);
        threadPoolTaskExecutor.setMaxPoolSize(500);
        threadPoolTaskExecutor.afterPropertiesSet();

        return steps.get("Merchant Process Step")
            .<TransactRead, MerchantBase>chunk(10000)
            .reader(transactionReader())
            .processor( new MerchantProcessor() )
            .writer(merchantXmlWriter())
            //.writer( new ConsoleItemWriter() )
            .allowStartIfComplete(true)
            //.taskExecutor(threadPoolTaskExecutor)
            .build();
    }
    
    @Bean
    public Job merchantJob() {
        return jobs.get("Merchant Process")
            .incrementer(new RunIdIncrementer())
            .start(merchantStep())
            .build();
    }
}
