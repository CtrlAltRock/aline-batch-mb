package com.smoothstack.transactionbatch.writer;


import java.util.List;

import com.smoothstack.transactionbatch.model.TransactRead;

import org.springframework.batch.item.support.AbstractItemStreamItemWriter;

public class ConsoleItemWriter extends AbstractItemStreamItemWriter<TransactRead> {
    
    @Override
    public void write(List<? extends TransactRead> transacts) throws Exception {
        System.out.printf("****** %s ******%n", transacts.get(0));
    }
}