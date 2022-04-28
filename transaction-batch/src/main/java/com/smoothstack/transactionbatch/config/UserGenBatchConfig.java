package com.smoothstack.transactionbatch.config;

import java.util.HashMap;

import com.smoothstack.transactionbatch.model.TransactRead;
import com.smoothstack.transactionbatch.model.UserBase;
import com.smoothstack.transactionbatch.processor.UserProcessor;
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

@Configuration
public class UserGenBatchConfig {
    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private TaskExecutor taskExecutor;
    
    @Autowired
    private TransactionReader transactionReader;

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
    public SynchronizedXML<UserBase> userXmlWriter() {
        FileSystemResource output = new FileSystemResource("output/GeneratedUsers.xml");

        return new SynchronizedXMLBuilder<UserBase>()
            .name("userXmlWriter")
            .resource(output)
            .marshaller(userXmlMarshaller())
            .rootTagName("GeneratedUsers")
            .build();
    }

    @Bean
    public Step userStep() {
        return steps.get("User Process Step")
            .<TransactRead, UserBase>chunk(10000)
            .reader(transactionReader.read())
            .processor( new UserProcessor() )
            .writer( userXmlWriter() )
            .allowStartIfComplete(true)
            .taskExecutor(taskExecutor)
            .build();
    }
}
