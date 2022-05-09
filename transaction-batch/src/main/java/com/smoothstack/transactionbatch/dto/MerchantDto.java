package com.smoothstack.transactionbatch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MerchantDto {
    private long id;
    private String city;
    private String state;
    private String zip;
    private int mcc;
}
