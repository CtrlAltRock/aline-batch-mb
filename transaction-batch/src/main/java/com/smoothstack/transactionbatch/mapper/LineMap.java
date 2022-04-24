package com.smoothstack.transactionbatch.mapper;

import com.smoothstack.transactionbatch.model.TransactRead;

import org.springframework.batch.item.file.mapping.DefaultLineMapper;

public class LineMap extends DefaultLineMapper<TransactRead> {
    @Override
    public TransactRead mapLine(String line, int sumn) {
        return new TransactRead();
    }
}
