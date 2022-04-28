package com.smoothstack.transactionbatch.model;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
@XmlRootElement
public class StateBase {
    private int id;
    private String abbrev;
    private String name;
    private String capital;
    private String nickname;
}
