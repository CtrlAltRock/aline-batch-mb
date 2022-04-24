package com.smoothstack.transactionbatch.writer;


import java.util.List;

import com.smoothstack.transactionbatch.model.UserBase;

import org.springframework.batch.item.support.AbstractItemStreamItemWriter;

public class ConsoleItemWriter extends AbstractItemStreamItemWriter<UserBase> {
    
    @Override
    public void write(List<? extends UserBase> transacts) throws Exception {
        if (transacts.size() > 0) System.out.printf("****** %s ******%n", transacts.get(0));
    }
}