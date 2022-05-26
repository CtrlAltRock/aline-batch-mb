package com.smoothstack.transactionbatch.report;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MerchantByCity {
    /*private final Map<String, Set<Long>> merchantsByCity = new ConcurrentHashMap<>();

    private final Map<String, Boolean> hasOnlineTransaction = new ConcurrentHashMap<>();

    public Map<String, Set<Long>> getMerchantsByCity() { return merchantsByCity; }

    public Map<String, Boolean> getOnlineTransactions() { return hasOnlineTransaction; }


    // Filter out merchants by city that have online transactions
    public Map<String, Set<Long>> getMerchantsByCityWithOnline() {
        Set<String> hasOnline = hasOnlineTransaction.entrySet().parallelStream()
            .filter(n -> n.getValue().booleanValue())
            .map(n -> n.getKey())
            .collect(Collectors.toSet());

        return merchantsByCity.entrySet().parallelStream()
            .filter(n -> hasOnline.contains(n.getKey()))
            .collect(Collectors.toMap(n -> n.getKey(), n -> n.getValue()));
    }

    public void addMerchantToCity(String city, long merchant) {
        
    }*/
}
