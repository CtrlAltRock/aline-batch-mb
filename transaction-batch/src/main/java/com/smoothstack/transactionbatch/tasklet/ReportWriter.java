package com.smoothstack.transactionbatch.tasklet;

import java.io.File;
import java.io.IOException;

import com.smoothstack.transactionbatch.tasklet.report.DepositWriter;
import com.smoothstack.transactionbatch.tasklet.report.FraudByYear;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReportWriter implements Tasklet {
    private final String basePath = new File("").getAbsolutePath();

    @Override
    public RepeatStatus execute(StepContribution contrib, ChunkContext cont) throws Exception {
        try {
            DepositWriter.write(basePath);
        } catch (IOException e) {
            log.error("Deposit File writer error: {}", e.getMessage());
        }

        try {
            FraudByYear.write(basePath);
        } catch (IOException e) {
            log.error("Fraud by Year writer error: {}", e.getMessage());
        }

        return RepeatStatus.FINISHED;
    }
}
