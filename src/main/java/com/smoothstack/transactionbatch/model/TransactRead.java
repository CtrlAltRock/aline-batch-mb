package com.smoothstack.transactionbatch.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactRead {
    private long user;
    private long card;
    private LocalDateTime date;
    private BigDecimal amount;
    private String use;
    private long merchant;
    private String city;
    private String state;
    private int zip;
    private int mcc;
    private String errors;
    private boolean isFraud;
}
