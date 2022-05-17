package com.smoothstack.transactionbatch.report;

import java.util.AbstractMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.dto.RecurringDto;
import com.smoothstack.transactionbatch.model.TransactRead;

public class Merchants implements ReportUtils {

    // String concatenates information of amount with merchant
    private AbstractMap<Long, AbstractMap<String, AtomicLong>> recurringTransactions = new ConcurrentHashMap<>();

    public AbstractMap<String, AtomicLong> getTransactions() { 
        AbstractMap<String, AtomicLong> recurrences = new ConcurrentHashMap<>();

        recurringTransactions.values()
        .forEach(n -> {
            recurrences.putAll(n);
        });

        return recurrences;
     }
    
    public void addTransaction(long merchant, String info) {
        // Add the Merchant
        if (!recurringTransactions.containsKey(merchant)) {
            synchronized (this.recurringTransactions) {
                if (!recurringTransactions.containsKey(merchant)) {
                    recurringTransactions.put(merchant, new ConcurrentHashMap<>());
                }
            }
        }

        AbstractMap<String, AtomicLong> merch = recurringTransactions.get(merchant);

        // Start recurring transactions for merchants
        if (!merch.containsKey(info)) {
            synchronized (this.recurringTransactions) {
                if (!merch.containsKey(info)) {
                    merch.put(info, new AtomicLong());
                }
            }
        }

        merch.get(info).incrementAndGet();
    }

    public int getNumOfMerchants() {
        return recurringTransactions.size();
    }

    @Override 
    public void addItems(Stream<? extends TransactRead> items) {
        items.forEach(n -> {
            RecurringDto recur = new RecurringDto(n.getMerchant(), n.getAmount(), n.getUser(), n.getCard());
            addTransaction(n.getMerchant(), recur.toString());
        });
    }

    @Override
    public void clearCache() {
        recurringTransactions.clear();
    }
}
