package com.smoothstack.transactionbatch.report;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.model.ErrorBase;
import com.smoothstack.transactionbatch.model.TransactRead;

public class ErrorsFound implements ReportUtils {
    // Map user id's to errors that user has
    private AbstractMap<Long, List<ErrorBase>> errorsFound = new ConcurrentHashMap<>();

    public AbstractMap<Long, List<ErrorBase>> getErrorsFound() { return errorsFound; }

    public Stream<ErrorBase> getErrors() { 
        return errorsFound.values().stream()
            .flatMap(n -> n.stream())
            .filter(n -> !n.getErrorMessage().isBlank());
    }

    public Stream<ErrorBase> getFrauds() {
        return errorsFound.values().stream()
            .flatMap(n -> n.stream())
            .filter(n -> n.isFraud());
    }

    public long getUserCount() { return errorsFound.size(); }

    public void makeError(ErrorBase error) {
        long user = error.getUserId();

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
            userErrors.add(error);
            
        }
    }

    @Override
    public void addItems(Stream<? extends TransactRead> items) {
        items.map(n -> new ErrorBase(n.getUser(), n.getDate(), n.getErrors(), n.getFraud()))
        .forEach(this::makeError);
    }

    
    @Override
    public void clearCache() {
        errorsFound.clear();
    }
}
