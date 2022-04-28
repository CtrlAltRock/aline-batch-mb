package com.smoothstack.transactionbatch.config;

import java.util.HashMap;

import com.smoothstack.transactionbatch.model.StateBase;
import com.smoothstack.transactionbatch.model.TransactRead;
import com.smoothstack.transactionbatch.processor.StateProcessor;
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
public class StateBatchConfig {
    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private TransactionReader transactionReader;

    @Bean
    @StepScope
    public XStreamMarshaller stateXmlMarshaller() {
        HashMap<String, Class<StateBase>> alias = new HashMap<>();
        alias.put("state", StateBase.class);

        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setAliases(alias);

        return marshaller;
    }

    @Bean
    @StepScope
    public SynchronizedXML<StateBase> stateXmlWriter() {
        FileSystemResource output = new FileSystemResource("output/GeneratedStates.xml");

        return new SynchronizedXMLBuilder<StateBase>()
            .name("stateXmlWriter")
            .resource(output)
            .marshaller(stateXmlMarshaller())
            .rootTagName("GeneratedStates")
            .build();
    }

    @Bean
    public Step stateStep() {
        return steps.get("State Process Step")
            .<TransactRead, StateBase>chunk(10000)
            .reader(transactionReader.read())
            .processor( new StateProcessor() )
            .writer( stateXmlWriter() )
            .allowStartIfComplete(true)
            .taskExecutor(taskExecutor)
            .build();
    }
}
