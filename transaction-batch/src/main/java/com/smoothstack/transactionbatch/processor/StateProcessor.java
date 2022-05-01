package com.smoothstack.transactionbatch.processor;

import com.smoothstack.transactionbatch.generator.StateGenerator;
import com.smoothstack.transactionbatch.model.TransactRead;

import org.springframework.batch.item.ItemProcessor;

public class StateProcessor implements ItemProcessor<TransactRead, TransactRead> {
    private static StateGenerator stateGenerator = StateGenerator.getInstance();

    @Override
    public TransactRead process(TransactRead item) throws Exception {
        stateGenerator.getState(item.getState());

        return item;
    }
}
