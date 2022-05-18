package com.smoothstack.transactionbatch.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class RecurringDto {
    private long merchantId;
    private BigDecimal amount;
    private long userId;
    private long cardId;
}
