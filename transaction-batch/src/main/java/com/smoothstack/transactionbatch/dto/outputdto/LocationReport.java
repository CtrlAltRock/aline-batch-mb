package com.smoothstack.transactionbatch.dto.outputdto;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@XmlRootElement
public class LocationReport {
    private String location;
    private long transactions;
}
