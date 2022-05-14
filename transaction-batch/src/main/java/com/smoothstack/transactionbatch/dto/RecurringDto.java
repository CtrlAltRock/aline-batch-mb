package com.smoothstack.transactionbatch.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecurringDto {
    private long merchantId;
    private BigDecimal amount;
    private long userId;
    private long cardId;

    @Override
    public String toString() {
        return String.format("%d %s %d %d", merchantId, amount.toPlainString(), userId, cardId);
    }
}
