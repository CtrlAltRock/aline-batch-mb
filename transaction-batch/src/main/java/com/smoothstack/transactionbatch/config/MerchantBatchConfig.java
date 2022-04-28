package com.smoothstack.transactionbatch.config;

import java.util.HashMap;

import com.smoothstack.transactionbatch.model.MerchantBase;
import com.smoothstack.transactionbatch.model.TransactRead;
import com.smoothstack.transactionbatch.processor.MerchantProcessor;
import com.smoothstack.transactionbatch.reader.TransactionReader;
import com.smoothstack.transactionbatch.writer.SynchronizedXML;
import com.smoothstack.transactionbatch.writer.SynchronizedXMLBuilder;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class MerchantBatchConfig {
    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private TransactionReader transactionReader;

    @Autowired
    private TaskExecutor taskExecutor;

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
    public SynchronizedXML<MerchantBase> merchantXmlWriter() {
        FileSystemResource output = new FileSystemResource("output/GeneratedMerchants.xml");

        return new SynchronizedXMLBuilder<MerchantBase>()
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
            .reader(transactionReader.read())
            .processor( new MerchantProcessor() )
            .writer(merchantXmlWriter())
            .allowStartIfComplete(true)
            .taskExecutor(taskExecutor)
            .build();
    }
}
