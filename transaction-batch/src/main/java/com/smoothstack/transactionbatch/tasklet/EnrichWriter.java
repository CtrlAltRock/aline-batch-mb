package com.smoothstack.transactionbatch.tasklet;

import java.io.File;
import java.io.IOException;

import com.smoothstack.transactionbatch.tasklet.module.CardWriter;
import com.smoothstack.transactionbatch.tasklet.module.MerchantWriter;
import com.smoothstack.transactionbatch.tasklet.module.UserWriter;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnrichWriter implements Tasklet {
    private final String basePath = new File("").getAbsolutePath();

    @Override
    public RepeatStatus execute(StepContribution contrib, ChunkContext cont) throws Exception {
        log.info("Hello??");
        try {
            UserWriter.write(basePath);
        } catch (IOException e) {
            log.error("User File writer error: {}", e.getMessage());
        }

        try {
            CardWriter.write(basePath);
        } catch (IOException e) {
            log.error("Card File writer error: {}", e.getMessage());
        }

        try {
            MerchantWriter.write(basePath);
        } catch (IOException e) {
            log.error("Merchant File writer error: {}", e.getMessage());
        }

        return RepeatStatus.FINISHED;
    }
}
