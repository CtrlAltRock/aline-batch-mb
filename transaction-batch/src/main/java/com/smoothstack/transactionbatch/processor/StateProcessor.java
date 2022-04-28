package com.smoothstack.transactionbatch.processor;

import java.util.Optional;

import com.smoothstack.transactionbatch.generator.StateGenerator;
import com.smoothstack.transactionbatch.model.StateBase;
import com.smoothstack.transactionbatch.model.TransactRead;

import org.springframework.batch.item.ItemProcessor;

public class StateProcessor implements ItemProcessor<TransactRead, StateBase> {
    private static StateGenerator stateGenerator = StateGenerator.getInstance();

    @Override
    public StateBase process(TransactRead item) throws Exception {
        Optional<StateBase> state = stateGenerator.getState(item.getState());

        if (state.isEmpty()) return null; else return state.get();
    }
}
