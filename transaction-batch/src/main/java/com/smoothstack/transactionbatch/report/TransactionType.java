package com.smoothstack.transactionbatch.report;

import java.util.AbstractMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.model.TransactRead;

public class TransactionType implements ReportUtils {
    private AbstractMap<String, AtomicLong> transactionTypes = new ConcurrentHashMap<>();

    public int getNumberOfTransactions() { return transactionTypes.size(); }

    public AbstractMap<String, AtomicLong> getTransactionTypes() { return transactionTypes; }

    public void addTransactionType(String transactionType) {
        if (!transactionTypes.containsKey(transactionType)) {
            synchronized (this.transactionTypes) {
                if (!transactionTypes.containsKey(transactionType)) {
                    transactionTypes.put(transactionType, new AtomicLong());
                }
            }
        }

        transactionTypes.get(transactionType).incrementAndGet();
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
