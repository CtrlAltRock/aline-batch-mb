package com.smoothstack.transactionbatch.generator;

import java.util.AbstractMap;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.github.javafaker.Faker;
import com.smoothstack.transactionbatch.model.UserBase;

public class UserGenerator {
    private static UserGenerator INSTANCE = null;

    private final Faker faker = new Faker();
    private static final AbstractMap<Long, UserBase> context = new ConcurrentHashMap<>();

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

    public AbstractMap<Long, UserBase> getContext() { return context; }

    // For each unique user id, generate a fake name and account information
    public Optional<UserBase> generateUser(long id) {
        if (!context.containsKey(id)) {
            synchronized (this) {
                if (!context.containsKey(id)) {
                    String firstName = faker.name().firstName();
                    String middleName = faker.name().firstName();
                    String lastName = faker.name().lastName();

                    Date dateOfBirth = faker.date().past(80 * 365, 18 * 365, TimeUnit.DAYS);

                    String email = firstName + "." + lastName + "@smoothstack.gov";

                    String phoneNumber = faker.phoneNumber().cellPhone();

                    String[] addr = faker.address().fullAddress().split(", ");
                    String[] stuff = addr[2].split(" ");

                    UserBase newUser = new UserBase(
                        id,
                        firstName,
                        middleName,
                        lastName,
                        dateOfBirth,
                        email,
                        phoneNumber,
                        addr[0],
                        addr[1],
                        stuff[0],
                        stuff[1]
                    );

                    context.put(id, newUser);

                    return Optional.of(newUser);
                }
            }
        }
        
        return Optional.empty();        
    }
}
