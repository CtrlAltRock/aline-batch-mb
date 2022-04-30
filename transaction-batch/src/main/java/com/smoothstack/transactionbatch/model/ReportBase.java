package com.smoothstack.transactionbatch.model;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;

@Getter
@XmlRootElement
public class ReportBase {
    private String title;
    private String report;
}
