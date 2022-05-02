package com.smoothstack.transactionbatch.outputdto;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@XmlRootElement
public class YearBy {
    private int year;
    private String percentage;

    public static YearBy makeYearBy(int year, String percent) {
        return new YearBy(year, percent);
    }
}
