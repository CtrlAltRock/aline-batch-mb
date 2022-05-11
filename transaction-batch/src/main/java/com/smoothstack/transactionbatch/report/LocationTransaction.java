package com.smoothstack.transactionbatch.report;

import java.util.AbstractMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.dto.LocationDto;

// Handle transactions by zip and by city
public class LocationTransaction {
    private final AbstractMap<Integer, AtomicLong> transactByZip = new ConcurrentHashMap<>();

    private final AbstractMap<String, AtomicLong> transactByCity = new ConcurrentHashMap<>();

    public AbstractMap<Integer, AtomicLong> getZipTransacts() { return transactByZip; }

    public AbstractMap<String, AtomicLong> getCityTransacts() { return transactByCity; }

    public void makeZipTransact(int zip) {
        if (!transactByZip.containsKey(zip)) {
            synchronized (this) {
                if (!transactByZip.containsKey(zip)) {
                    transactByZip.put(zip, new AtomicLong());
                }
            }
        }

        transactByZip.get(zip).incrementAndGet();
    }

    public void makeCityTransact(String city) {
        if (!transactByCity.containsKey(city)) {
            synchronized (this) {
                if (!transactByCity.containsKey(city)) {
                    transactByCity.put(city, new AtomicLong());
                }
            }
        }
        
        // Does not need synchronization because of atomiclong
        transactByCity.get(city).incrementAndGet();
    }

    public void makeTransactions(Stream<LocationDto> locations) {
        locations.forEach(n -> {
            makeZipTransact(n.getZip());
            makeCityTransact(n.getCity());
        });
    }

    public void clearMaps() {
        transactByCity.clear();
        transactByZip.clear();
    }
}
