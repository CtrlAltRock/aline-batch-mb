package com.smoothstack.transactionbatch.model;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@XmlRootElement
public class MerchantBase {
    private long id;
    private String name;
    private String city;
    private String state;
    private String zip;
    private String mcc;
}
