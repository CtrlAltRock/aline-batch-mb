package com.smoothstack.transactionbatch.report;

import java.time.YearMonth;
import java.util.AbstractMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.dto.OnlineMonthlyDto;
import com.smoothstack.transactionbatch.model.TransactRead;

public class TransactionType implements ReportUtils {
    private AbstractMap<String, AtomicLong> transactionTypes = new ConcurrentHashMap<>();

    private AbstractMap<OnlineMonthlyDto, AtomicLong> monthlyTransact = new ConcurrentHashMap<>();

    public int getNumberOfTransactions() { return transactionTypes.size(); }

    public AbstractMap<String, AtomicLong> getTransactionTypes() { return transactionTypes; }

    public AbstractMap<OnlineMonthlyDto, AtomicLong> getMonthlyTransact() { return monthlyTransact; }

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

    public void addMonthlyTransacts(OnlineMonthlyDto onlineMonthlyDto) {
        if (!monthlyTransact.containsKey(onlineMonthlyDto)) {
            synchronized (this.monthlyTransact) {
                if (!monthlyTransact.containsKey(onlineMonthlyDto)) {
                    monthlyTransact.put(onlineMonthlyDto, new AtomicLong());
                }
            }
        }

        monthlyTransact.get(onlineMonthlyDto).incrementAndGet();
    }
    
    @Override
    public void addItems(Stream<? extends TransactRead> items) {
        items.forEach(n -> {
            addTransactionType(n.getUse());

            if (n.getUse().equals("Online Transaction")) {
                addMonthlyTransacts(
                    new OnlineMonthlyDto(n.getUse(), YearMonth.from(n.getDate()))
                );
            }
        });
    }
    
    @Override
    public void clearCache() {
        transactionTypes.clear();
        monthlyTransact.clear();
    }
}
