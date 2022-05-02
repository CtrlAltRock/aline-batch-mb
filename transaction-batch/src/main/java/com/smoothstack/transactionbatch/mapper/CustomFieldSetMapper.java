package com.smoothstack.transactionbatch.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.smoothstack.transactionbatch.model.TransactRead;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class CustomFieldSetMapper implements FieldSetMapper<TransactRead> {
    public TransactRead mapFieldSet(FieldSet fieldSet) {
        long user = fieldSet.readLong("user");
        long card = fieldSet.readLong("card");

        String[] time = fieldSet.readString("time").split(":");
        int hour = Integer.valueOf(time[0]);
        int minute = Integer.valueOf(time[1]);

        LocalDateTime date = LocalDateTime.of(
            fieldSet.readInt("year"), 
            fieldSet.readInt("month"), 
            fieldSet.readInt("day"),
            hour,
            minute 
            );

        BigDecimal amount = new BigDecimal(fieldSet.readString("amount").substring(1));
        String use = fieldSet.readString("use");
        long merchant = fieldSet.readLong("merchant");
        String city = fieldSet.readString("city");
        String state = fieldSet.readString("state");
        String zip = fieldSet.readString("zip");
        int mcc = fieldSet.readInt("mcc");
        String errors = fieldSet.readString("errors");

        boolean isFraud = false;

        if (fieldSet.readString("isFraud").equals("Yes")) isFraud = true;

        return new TransactRead(user, card, date, amount, use, merchant, city, state, zip, mcc, errors, isFraud);
    }
}
