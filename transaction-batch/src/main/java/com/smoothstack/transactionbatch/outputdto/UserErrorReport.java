package com.smoothstack.transactionbatch.outputdto;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@XmlRootElement
public class UserErrorReport {
    private String errorMessage;
    private String percent;
}
