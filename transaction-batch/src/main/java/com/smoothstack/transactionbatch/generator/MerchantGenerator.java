package com.smoothstack.transactionbatch.generator;

import java.util.AbstractMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.github.javafaker.Faker;
import com.smoothstack.transactionbatch.dto.MerchantDto;
import com.smoothstack.transactionbatch.model.MerchantBase;

public class MerchantGenerator {
    private static MerchantGenerator INSTANCE = null;

    private final Faker faker = new Faker();
    private static final AbstractMap<Long, MerchantBase> context = new ConcurrentHashMap<>();

    private MerchantGenerator() {}

    public static MerchantGenerator getInstance() {
        if (INSTANCE == null) {
            synchronized(UserGenerator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MerchantGenerator();
                }
            }
        }
        return INSTANCE;
    }

    public Optional<MerchantBase> generateMerchant(MerchantDto merch) {
        long id = merch.getId();
        if (!context.containsKey(id)) {
            synchronized (this) {
                if (!context.containsKey(id)) {
                    String name = faker.company().name();
                    MerchantBase newMerchant = new MerchantBase(id, name, merch.getCity(), merch.getState(), merch.getZip(), merch.getMcc());

                    context.put(id, newMerchant);

                    return Optional.of(newMerchant);
                }
            }
        }

        return Optional.empty();
    }
}
