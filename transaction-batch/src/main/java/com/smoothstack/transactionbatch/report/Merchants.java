package com.smoothstack.transactionbatch.report;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.dto.RecurringDto;

public class Merchants {
    private Set<Long> uniqueMerchants = new HashSet<>();

    // String concatenates information of amount with merchant
    private AbstractMap<String, AtomicLong> recurringTransactions = new ConcurrentHashMap<>();

    public AbstractMap<String, AtomicLong> getTransactions() { return recurringTransactions; }
    
    public void addTransaction(String key) {
        if (!recurringTransactions.containsKey(key)) {
            synchronized (this.recurringTransactions) {
                if (!recurringTransactions.containsKey(key)) {
                    recurringTransactions.put(key, new AtomicLong());
                }
            }
        }

        recurringTransactions.get(key).incrementAndGet();
    }

    public void addTransactions(Stream<RecurringDto> transacts) {
        transacts.forEach(n -> addTransaction(n.toString()));
    }

    public synchronized void addMerchants(Stream<Long> merchants) {
        merchants.forEach(n -> uniqueMerchants.add(n));
    }

    public int getNumOfMerchants() {
        return uniqueMerchants.size();
    }

    public void clear() {
        uniqueMerchants.clear();
    }
}
