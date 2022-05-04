package com.smoothstack.transactionbatch.tasklet;

import java.io.File;

import com.smoothstack.transactionbatch.report.Deposit;
import com.smoothstack.transactionbatch.report.ErrorsFound;
import com.smoothstack.transactionbatch.tasklet.report.DepositWriter;
import com.smoothstack.transactionbatch.tasklet.report.FraudByYear;
import com.smoothstack.transactionbatch.tasklet.report.InsufficientOnce;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class ReportWriter implements Tasklet {
    private final String basePath = new File("").getAbsolutePath();

    private final Deposit depo = Deposit.getInstance();

    private final ErrorsFound errors = ErrorsFound.getInstance();

    @Override
    public RepeatStatus execute(StepContribution contrib, ChunkContext cont) throws Exception {
        try {            
            DepositWriter.write(depo, basePath);
        } finally {
            depo.clearMap();
        }

        try {
            FraudByYear.write(errors, basePath);

            InsufficientOnce.write(errors, basePath);
        } finally {
            errors.clearMap();
        }


        return RepeatStatus.FINISHED;
    }
}
