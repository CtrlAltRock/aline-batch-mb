package com.smoothstack.transactionbatch.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public final class TransactionTimeDto implements Comparable<TransactionTimeDto>{
    private LocalDateTime transactionTime;
    private BigDecimal amount;


    // Get transactions largest to smallest
    @Override
    public int compareTo(TransactionTimeDto other) {
        return other.getAmount().compareTo(this.amount);
    }
}
