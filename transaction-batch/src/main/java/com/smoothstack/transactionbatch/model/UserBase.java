package com.smoothstack.transactionbatch.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class UserBase {
    private long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private Date dateOfBirth;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String zip;

}
