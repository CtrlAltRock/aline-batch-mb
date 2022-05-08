package com.smoothstack.transactionbatch.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class FileDelegator {
    private static Map<String, AtomicLong> delegator = new ConcurrentHashMap<>();

    public static String getFileName(String context) {
        if (!delegator.containsKey(context)) {
            synchronized (FileDelegator.class) {
                if (!delegator.containsKey(context)) {
                    delegator.put(context, new AtomicLong());
                }
            }
        }

        return new StringBuilder()
            .append(context)
            .append(delegator.get(context).incrementAndGet())
            .toString();
    }
}
