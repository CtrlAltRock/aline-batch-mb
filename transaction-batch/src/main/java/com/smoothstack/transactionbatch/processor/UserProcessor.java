package com.smoothstack.transactionbatch.processor;

import com.smoothstack.transactionbatch.generator.UserGenerator;
import com.smoothstack.transactionbatch.model.TransactRead;

import org.springframework.batch.item.ItemProcessor;

public class UserProcessor implements ItemProcessor<TransactRead, TransactRead> {
    private static UserGenerator userGenerator = UserGenerator.getInstance();

    @Override
    public TransactRead process(TransactRead item) throws Exception {
        userGenerator.generateUser(item.getUser());

        return item;
    }
}
