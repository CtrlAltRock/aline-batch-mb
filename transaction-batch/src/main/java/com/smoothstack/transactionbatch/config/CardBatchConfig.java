package com.smoothstack.transactionbatch.config;

import java.util.HashMap;

import com.smoothstack.transactionbatch.model.CardBase;
import com.smoothstack.transactionbatch.model.TransactRead;
import com.smoothstack.transactionbatch.processor.CardProcessor;
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
public class CardBatchConfig {
    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private TransactionReader transactionReader;

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
    public SynchronizedXML<CardBase> cardXmlWriter() {
        FileSystemResource output = new FileSystemResource("output/GeneratedCards.xml");

        return new SynchronizedXMLBuilder<CardBase>()
            .name("cardXmlWriter")
            .resource(output)
            .marshaller(cardXmlMarshaller())
            .rootTagName("GeneratedCards")
            .build();
    }

    @Bean
    public Step cardStep() {
        return steps.get("Card Process Step")
            .<TransactRead, CardBase>chunk(100000)
            .reader(transactionReader.read())
            .processor( new CardProcessor() )
            .writer(cardXmlWriter())
            .allowStartIfComplete(true)
            .taskExecutor(taskExecutor)
            .build();
    }
}
