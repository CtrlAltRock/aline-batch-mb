package com.smoothstack.transactionbatch.report;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.dto.TransactionTimeDto;
import com.smoothstack.transactionbatch.model.TransactRead;

public class TopTenTransaction implements ReportUtils {
    // Here for when we add other stories
    //private AbstractMap<LocalDateTime, BigDecimal> transactions = new ConcurrentHashMap<>();

    private Collection<TransactionTimeDto> topTenTransactions = null;

    public Collection<TransactionTimeDto> getTopTen() { return topTenTransactions; }
        
    public synchronized void aggregateTopTen(Stream<TransactionTimeDto> items) {
        if (topTenTransactions == null) {
            topTenTransactions = items.sorted().limit(10).collect(Collectors.toList());
        } else {
            topTenTransactions = Stream.concat(items, topTenTransactions.stream()).sorted().limit(10).collect(Collectors.toList());
        }

    }

    @Override
    public void addItems(Stream<? extends TransactRead> items) {
        aggregateTopTen(items.map(n -> new TransactionTimeDto(n.getDate(), n.getAmount())));
    }

    @Override
    public void clearCache() {

    }
}
