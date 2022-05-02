package com.smoothstack.transactionbatch.report;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.smoothstack.transactionbatch.model.ErrorBase;

public class ErrorsFound {
    // Map user id's to errors that user has
    private AbstractMap<Long, List<ErrorBase>> errorsFound = new ConcurrentHashMap<>();
    private static ErrorsFound INSTANCE = null;

    private ErrorsFound() {}

    public static ErrorsFound getInstance() {
        if (INSTANCE == null) {
            synchronized(Deposit.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ErrorsFound();
                }
            }
        }
        return INSTANCE;
    }

    public AbstractMap<Long, List<ErrorBase>> getBalances() { return errorsFound; }

    public void makeError(long user, LocalDateTime transactionTime, String message, boolean isFraud) {
        if (!errorsFound.containsKey(user)) {
            synchronized (this) {
                if (!errorsFound.containsKey(user)) {
                    errorsFound.put(user, new ArrayList<>());
                }
            }
        }
        
        // ArrayList is unsynchronized
        synchronized (this) {
            List<ErrorBase> userErrors = errorsFound.get(user);
            userErrors.add(new ErrorBase(user, transactionTime, message, isFraud));
        }
    }
}
