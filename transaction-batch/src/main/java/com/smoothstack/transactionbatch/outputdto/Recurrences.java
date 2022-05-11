package com.smoothstack.transactionbatch.outputdto;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@XmlRootElement
public class Recurrences {
    private String merchantId;
    private String amount;
    private String userId;
    private String cardId;
    private long occurences;
}
