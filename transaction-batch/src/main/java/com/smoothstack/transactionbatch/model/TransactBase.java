package com.smoothstack.transactionbatch.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@XmlRootElement
public class TransactBase {
    private long id;
    private long merchant;
    private String city;
    private String zip;
    private String use;
    private BigDecimal amount;
}
