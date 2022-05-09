package com.smoothstack.transactionbatch.report;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class Merchants {
    private Set<Long> uniqueMerchants = new HashSet<>();

    public synchronized void addMerchants(Stream<Long> merchants) {
        merchants.forEach(n -> uniqueMerchants.add(n));
    }

    public int getNumOfMerchants() {
        return uniqueMerchants.size();
    }

    public void clear() {
        uniqueMerchants.clear();
    }
}
