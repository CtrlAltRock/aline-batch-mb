package com.smoothstack.transactionbatch.report;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.model.TransactRead;

public class MerchantByCity implements ReportUtils {
    // Set of merchants that have an online transaction
    private final Set<Long> merchantWithOnline = Collections.synchronizedSet(new HashSet<>());
    
    private final Map<String, Set<Long>> merchantsByCity = new ConcurrentHashMap<>();

    public Map<String, Set<Long>> getMerchantsByCity() { return merchantsByCity; }

    public Set<Long> getMerchantWithOnline() { return merchantWithOnline; }

    // Filter out merchants by city that have online transactions
    public Stream<Map.Entry<String, Set<Long>>> getMerchantsByCityWithOnline() {
        synchronized (this.merchantsByCity) {
            return merchantsByCity.entrySet().parallelStream()
                .filter((n) -> {
                    // O(m * n) where m is number of keys in merchantsByCity, and n is number of elements in each value of merchantsByCity
                    for (long elem : n.getValue()) {
                        if (merchantWithOnline.contains(elem)) {
                            return true;
                        }
                    }
                    return false;
                });
        }
    }

    public void addMerchantToCity(String city, long merchant) {
        if (!merchantsByCity.containsKey(city)) {
            synchronized (merchantsByCity) {
                if (!merchantsByCity.containsKey(city)) {
                    merchantsByCity.put(city, Collections.synchronizedSet(new HashSet<>()));
                }
            }
        }

        
        merchantsByCity.get(city).add(merchant);
    }

    public void addItems(Stream<? extends TransactRead> items) {
        items.forEach(n -> {
            if (n.getCity().replaceAll("\\s", "").equals("ONLINE")) {
                merchantWithOnline.add(n.getMerchant());
            } else {
                addMerchantToCity(n.getCity(), n.getMerchant());
            }
        });
    }

    public void clearCache() {
        merchantWithOnline.clear();
        merchantsByCity.clear();
    }
} 
