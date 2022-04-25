package com.smoothstack.transactionbatch.processor;

import java.util.Optional;

import com.smoothstack.transactionbatch.generator.UserGenerator;
import com.smoothstack.transactionbatch.model.TransactRead;
import com.smoothstack.transactionbatch.model.UserBase;

import org.springframework.batch.item.ItemProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserProcessor implements ItemProcessor<TransactRead, UserBase> {
    private static UserGenerator userGenerator = UserGenerator.getInstance();


    @Override
    public UserBase process(TransactRead item) throws Exception {
        String address = item.getCity() + "|" + item.getZip();
        Optional<UserBase> user = userGenerator.generateUser(item.getUser());

        //log.info("{}", user.orElse(new UserBase()));

        if (user.isEmpty()) return null; else return user.get();
    }
}
