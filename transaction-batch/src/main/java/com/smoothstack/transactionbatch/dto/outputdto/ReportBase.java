package com.smoothstack.transactionbatch.dto.outputdto;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@XmlRootElement
public class ReportBase {
    private String title;
    private String report;
}
