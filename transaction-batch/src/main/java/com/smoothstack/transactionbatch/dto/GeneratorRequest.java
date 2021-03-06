package com.smoothstack.transactionbatch.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GeneratorRequest {
    private Boolean dataEnrich;
    private Boolean dataAnalyze;
}
