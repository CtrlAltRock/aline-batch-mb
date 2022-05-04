package com.smoothstack.transactionbatch.tasklet;

import java.io.File;

import com.smoothstack.transactionbatch.generator.CardGenerator;
import com.smoothstack.transactionbatch.generator.MerchantGenerator;
import com.smoothstack.transactionbatch.generator.UserGenerator;
import com.smoothstack.transactionbatch.tasklet.module.CardWriter;
import com.smoothstack.transactionbatch.tasklet.module.MerchantWriter;
import com.smoothstack.transactionbatch.tasklet.module.UserWriter;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class EnrichWriter implements Tasklet {
    private final String basePath = new File("").getAbsolutePath();

    private final UserGenerator users = UserGenerator.getInstance();
    private final CardGenerator cards = CardGenerator.getInstance();
    private final MerchantGenerator merchants = MerchantGenerator.getInstance();

    @Override
    // Try blocks are for clearing hashmap after we write, whether we error or not. Error isn't handled here
    public RepeatStatus execute(StepContribution contrib, ChunkContext cont) throws Exception {
        try {
            UserWriter.write(users, basePath);
        } finally {
            users.clearMap();
        }

        try {
            CardWriter.write(cards, basePath);
        } finally {
            cards.clearMap();
        }

        try {
            MerchantWriter.write(merchants, basePath);
        } finally {
            merchants.clearMap();
        }

        return RepeatStatus.FINISHED;
    }
}
