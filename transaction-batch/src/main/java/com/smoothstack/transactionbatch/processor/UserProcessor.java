package com.smoothstack.transactionbatch.processor;

import java.util.Optional;

import com.smoothstack.transactionbatch.generator.UserGenerator;
import com.smoothstack.transactionbatch.model.TransactRead;
import com.smoothstack.transactionbatch.model.UserBase;

import org.springframework.batch.item.ItemProcessor;

public class UserProcessor implements ItemProcessor<TransactRead, UserBase> {
    private static UserGenerator userGenerator = UserGenerator.getInstance();


    @Override
    public UserBase process(TransactRead item) throws Exception {
        Optional<UserBase> user = userGenerator.generateUser(item.getUser());

        if (user.isEmpty()) return null; else return user.get();
    }
}
