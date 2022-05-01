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
    private BigDecimal currentBalance;

    public DepositBase() {
        this.currentBalance = new BigDecimal(0).setScale(2);
    }
}
