package com.smoothstack.transactionbatch.generator;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.github.javafaker.Faker;
import com.smoothstack.transactionbatch.dto.MerchantDto;
import com.smoothstack.transactionbatch.model.MerchantBase;

public class MerchantGenerator {
    private static MerchantGenerator INSTANCE = null;

    private final Faker faker = new Faker();
    private final Set<Long> context = new HashSet<>();

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

    public Set<Long> getContext() { return context; }

    public Optional<MerchantBase> generateMerchant(MerchantDto merch) {
        long id = merch.getId();
        if (!context.contains(id)) {
            synchronized (this) {
                if (!context.contains(id)) {
                    String name = faker.company().name();
                    MerchantBase newMerchant = new MerchantBase(id, name, merch.getCity(), merch.getState(), merch.getZip(), merch.getMcc());

                    context.add(id);

                    return Optional.of(newMerchant);
                }
            }
        }

        return Optional.empty();
    }

    // Clean up after writing
    public void clearMap() {
        context.clear();
    }
}
