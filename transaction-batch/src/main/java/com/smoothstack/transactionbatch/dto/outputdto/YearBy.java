package com.smoothstack.transactionbatch.dto.outputdto;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@XmlRootElement
public class YearBy {
    private String title;
    private int year;
    private String percentage;
}
