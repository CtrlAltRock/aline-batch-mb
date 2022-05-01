package com.smoothstack.transactionbatch.processor.analysis;

import com.smoothstack.transactionbatch.model.TransactRead;
import com.smoothstack.transactionbatch.report.Deposit;

import org.springframework.batch.item.ItemProcessor;

public class DepositProcessor implements ItemProcessor<TransactRead, TransactRead> {
    private static Deposit depo = Deposit.getInstance();

    @Override
    public TransactRead process(TransactRead item) {
        depo.makeTransaction(item.getUser(), item.getAmount());
        
        return item;
    }
    
}
