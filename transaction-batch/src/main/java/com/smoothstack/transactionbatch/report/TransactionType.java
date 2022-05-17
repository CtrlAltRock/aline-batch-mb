package com.smoothstack.transactionbatch.report;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.model.TransactRead;

public class TransactionType implements ReportUtils {
    private Set<String> transactionTypes = new HashSet<>();

    public int getNumberOfTransactions() { return transactionTypes.size(); }

    public void addTransactionType(String transactionType) {
        if (!transactionTypes.contains(transactionType)) {
            synchronized (this.transactionTypes) {
                if (!transactionTypes.contains(transactionType)) {
                    transactionTypes.add(transactionType);
                }
            }
        }
    }
    
    @Override
    public void addItems(Stream<? extends TransactRead> items) {
        items.forEach(n -> addTransactionType(n.getUse()));
    }
    
    @Override
    public void clearCache() {
        transactionTypes.clear();
    }
}
