package com.smoothstack.transactionbatch.tasklet.report;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.smoothstack.transactionbatch.dto.outputdto.ReportBase;
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

        ReportBase report = TransactionTypeReport.generateReport(reportsContainer.getTransactionTypes())
            .sorted((n1, n2) -> Long.compare(Long.parseLong(n2.getReport()), Long.parseLong(n1.getReport())))
            .toList().get(0);

        assertEquals("Swipe Transaction", report.getTitle());
        assertEquals("2862", report.getReport());
    }

    @Test
    public void bottomMonthlyTest() {
        List<ReportBase> reports = BottomOnlineMonthly.generateReport(reportsContainer.getMonthlyTransact()).toList();

        assertEquals(5, reports.size());

        ReportBase report = reports.get(0);

        assertEquals("1", report.getReport());
    }
}
