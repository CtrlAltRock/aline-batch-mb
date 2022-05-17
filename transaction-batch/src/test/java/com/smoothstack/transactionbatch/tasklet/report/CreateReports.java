package com.smoothstack.transactionbatch.tasklet.report;

import java.util.List;

import com.smoothstack.transactionbatch.model.TransactRead;
import com.smoothstack.transactionbatch.report.GetTransactions;
import com.smoothstack.transactionbatch.report.ReportsContainer;

public class CreateReports {
    private ReportsContainer reportsContainer = ReportsContainer.getInstance();

    private static CreateReports INSTANCE = null;

    private CreateReports() {
        List<TransactRead> transacts = new GetTransactions().getTransactions().toList();

        reportsContainer.addItems(transacts);
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
