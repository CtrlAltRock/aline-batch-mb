package com.smoothstack.transactionbatch.report;

import java.util.stream.Stream;

import com.smoothstack.transactionbatch.model.TransactRead;

public interface ReportUtils {
    // Logic for populating each reporter
    public void addItems(Stream<? extends TransactRead> items);

    // Implement clearing for each reporter
    public void clearCache();
}
