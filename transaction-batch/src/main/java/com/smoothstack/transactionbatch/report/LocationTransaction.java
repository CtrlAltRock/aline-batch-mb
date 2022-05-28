package com.smoothstack.transactionbatch.report;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.model.TransactRead;

// Handle transactions by zip and by city
public class LocationTransaction implements ReportUtils {
    private final AbstractMap<Integer, AtomicLong> transactByZip = new ConcurrentHashMap<>();

    private final AbstractMap<String, AtomicLong> transactByCity = new ConcurrentHashMap<>();

    private final AbstractMap<String, AtomicLong> afterEightPM = new ConcurrentHashMap<>();

    public AbstractMap<Integer, AtomicLong> getZipTransacts() { return transactByZip; }

    public AbstractMap<String, AtomicLong> getCityTransacts() { return transactByCity; }

    public AbstractMap<String, AtomicLong> getAfterEightPm() { return afterEightPM; }

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

    public void makeTimeTransact(String location, BigDecimal amount, LocalDateTime time) {
        if (checkTimeTransaction(amount, time)) return;

        if (!afterEightPM.containsKey(location)) {
            synchronized (this.afterEightPM) {
                if (!afterEightPM.containsKey(location)) {
                    afterEightPM.put(location, new AtomicLong());
                }
            }
        }

        afterEightPM.get(location).getAndIncrement();
    }

    public boolean checkTimeTransaction(BigDecimal amount, LocalDateTime time) {
        return (time.getHour() >= 20 && amount.compareTo(new BigDecimal("100.00")) != -1) ? true : false;
    }

    @Override
    public void addItems(Stream<? extends TransactRead> items) {
        // First filter out online transactions
        items.forEach((n) -> {
            String location = "ONLINE";
            
            if (!n.getCity().replaceAll("\\s", "").equals("ONLINE") && !n.getZip().isBlank()) {
                location = n.getZip().substring(0, n.getZip().length() - 2);
                makeZipTransact(Integer.parseInt(location));
                makeCityTransact(n.getCity());
            }

            makeTimeTransact(location, n.getAmount(), n.getDate());              
        });
    } 

    @Override
    public void clearCache() {
        transactByCity.clear();
        transactByZip.clear();
    }
}
