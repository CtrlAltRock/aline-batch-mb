package com.smoothstack.transactionbatch.tasklet.report;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.smoothstack.transactionbatch.report.ReportsContainer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransactionTypeTests {
    private ReportsContainer reportsContainer;

    @BeforeEach
    public void setUp() {
        reportsContainer = CreateReports.getInstance().getReports();
    }

    @Test
    public void typesTest() {
        assertEquals(2, reportsContainer.getNumberOfTransactions());
    }
}
