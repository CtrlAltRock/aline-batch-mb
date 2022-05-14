package com.smoothstack.transactionbatch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocationDto {
    private int zip;
    private String city;
}
