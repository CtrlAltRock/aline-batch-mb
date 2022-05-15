package com.smoothstack.transactionbatch.report;

import java.util.List;

import com.smoothstack.transactionbatch.model.TransactRead;

public class CreateReports {
    private ReportsContainer reportsContainer = ReportsContainer.getInstance();

    private static CreateReports INSTANCE = null;

    private CreateReports() {
        List<TransactRead> transacts = new GetTransactions().getTransactions().toList();

        reportsContainer.makeErrors(transacts);
        reportsContainer.addMerchants(transacts);
        reportsContainer.makeLocationTransacts(transacts);
        reportsContainer.makeRecurringTransacts(transacts);
    }

    public static CreateReports getInstance() {
        if (INSTANCE == null) {
            synchronized (CreateReports.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CreateReports();
                }
            }
        }

        return INSTANCE;
    }

    public ReportsContainer getReports() { return reportsContainer; }
}
