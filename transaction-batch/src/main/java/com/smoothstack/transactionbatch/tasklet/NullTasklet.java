package com.smoothstack.transactionbatch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class NullTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contrib, ChunkContext cont) throws Exception {
        return RepeatStatus.FINISHED;
    }
}
