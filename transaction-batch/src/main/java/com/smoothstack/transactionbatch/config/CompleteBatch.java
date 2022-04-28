package com.smoothstack.transactionbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@EnableBatchProcessing
@Configuration
public class CompleteBatch {
    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private CardBatchConfig cardBatchConfig;

    @Autowired
    private UserGenBatchConfig userGenBatchConfig;

    @Autowired
    private MerchantBatchConfig merchantBatchConfig;

    @Autowired
    private StateBatchConfig stateBatchConfig;

    @Autowired
    private TaskExecutor threadPoolTaskExecutor;

    @Bean
    public Flow userFlow() {
        return new FlowBuilder<SimpleFlow>("User Flow")
            .start(userGenBatchConfig.userStep())
            .from(userGenBatchConfig.userStep()).on("*").end()
            .build();
    }

    @Bean
    public Flow cardFlow() {
        return new FlowBuilder<SimpleFlow>("Card Flow")
            .start(cardBatchConfig.cardStep())
            .from(cardBatchConfig.cardStep()).on("*").end()
            .build();
    }

    @Bean
    public Flow merchantFlow() {
        return new FlowBuilder<SimpleFlow>("Merchant Flow")
            .start(merchantBatchConfig.merchantStep())
            .from(merchantBatchConfig.merchantStep()).on("*").end()
            .build();
    }

    @Bean
    public Flow stateFlow() {
        return new FlowBuilder<SimpleFlow>("State Flow")
            .start(stateBatchConfig.stateStep())
            .from(stateBatchConfig.stateStep()).on("*").end()
            .build();
    }

    @Bean
    public Flow controlFlow() {
        

        return new FlowBuilder<SimpleFlow>("Control Flow")
            .split(threadPoolTaskExecutor)
            .add(userFlow())
            .next(cardFlow())
            .next(merchantFlow())
            .next(stateFlow())
            .build();
    }

    @Bean
    public Job job() {
        return jobs.get("Full Batch")
            .incrementer(new RunIdIncrementer())
            .start(controlFlow())
            .end()
            .build();
    }
}
