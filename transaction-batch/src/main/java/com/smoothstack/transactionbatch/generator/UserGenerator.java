package com.smoothstack.transactionbatch.generator;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.github.javafaker.Faker;
import com.smoothstack.transactionbatch.context.ContextHolder;
import com.smoothstack.transactionbatch.model.UserBase;


public class UserGenerator {
    private static UserGenerator INSTANCE = null;

    private final Faker faker = new Faker();
    private final ContextHolder context = new ContextHolder();

    private final String onlineAddress = "12345 Street St.";
    private final String onlineZip = "66666";
    private final String onlineCity = "Townsville";


    private UserGenerator() {}

    public static UserGenerator getInstance() {
        if (INSTANCE == null) {
            synchronized(UserGenerator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserGenerator();
                }
            }
        }
        return INSTANCE;
    }

    public Optional<UserBase> generateUser(long id, String address) {
        if (!context.doesUserExist(id)) return Optional.empty();

        String firstName = faker.name().firstName();
        String middleName = faker.name().firstName();
        String lastName = faker.name().lastName();

        Date dateOfBirth = faker.date().past(80 * 365, 18 * 365, TimeUnit.DAYS);

        String email = firstName + "." + lastName + "@smoothstack.gov";

        String phoneNumber = faker.phoneNumber().phoneNumber();

        String addr;
        String zip;
        String city;

        if (address.equals("Online")) {
            addr = onlineAddress;
            zip = onlineZip;
            city = onlineCity;
        } else {
            String[] pieces = address.split("|");
            addr = faker.address().streetAddress();
            city = pieces[0];
            zip = pieces[1];
        }

        UserBase newUser = new UserBase(
            id,
            firstName,
            middleName,
            lastName,
            dateOfBirth,
            email,
            phoneNumber,
            addr,
            city,
            zip
        );

        return Optional.of(newUser);
    }
}
