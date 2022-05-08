package com.smoothstack.transactionbatch.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement
public class DepositBase {
    private long id;
    private BigDecimal amount;

    public DepositBase(long user) {
        this.id = user;
        this.amount = new BigDecimal(0).setScale(2);
    }

    public DepositBase(long user, BigDecimal amount) {
        this.id = user;
        this.amount = amount;
    }
}
