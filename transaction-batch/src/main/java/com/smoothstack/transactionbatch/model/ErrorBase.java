package com.smoothstack.transactionbatch.model;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@XmlRootElement
public class ErrorBase {
    private long userId;
    private LocalDateTime transactionTime;
    private String errorMessage;
    private boolean fraud;
}
