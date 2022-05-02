package com.smoothstack.transactionbatch.processor.analysis;

import java.math.BigDecimal;

import com.smoothstack.transactionbatch.model.TransactRead;
import com.smoothstack.transactionbatch.report.Deposit;

import org.springframework.batch.item.ItemProcessor;

public class DepositProcessor implements ItemProcessor<TransactRead, TransactRead> {
    private static Deposit depo = Deposit.getInstance();

    @Override
    public TransactRead process(TransactRead item) {
        BigDecimal amount = item.getAmount();
        
        // Only treat negative amounts as deposits
        if (amount.compareTo(BigDecimal.ZERO) != -1) return item;

        depo.makeDeposit(item.getUser(), amount.abs());
        
        return item;
    }
    
}
