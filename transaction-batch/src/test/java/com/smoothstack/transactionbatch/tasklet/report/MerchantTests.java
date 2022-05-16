package com.smoothstack.transactionbatch.tasklet.report;

import com.smoothstack.transactionbatch.dto.outputdto.Recurrences;
import com.smoothstack.transactionbatch.report.ReportsContainer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

public class MerchantTests {
    private ReportsContainer reportsContainer;

    @BeforeEach
    public void setUp() {
        reportsContainer = CreateReports.getInstance().getReports();
    }

    @Test
    public void uniqueMerchantsTest() {
        assertEquals(195, reportsContainer.getNumOfMerchants());
    }

    @Test
    public void recurringTransactsTest() {
        List<Recurrences> recurring = RecurringReport.generateReports(reportsContainer.getRecurring())
            .collect(Collectors.toList());

        assertEquals(5, recurring.size());

        Recurrences recurrence = recurring.get(0);

        assertEquals("$140.00", recurrence.getAmount());
        assertEquals("0", recurrence.getCardId());
        assertEquals("-4282466774399734331", recurrence.getMerchantId());
        assertEquals("0", recurrence.getUserId());
        assertEquals(20, recurrence.getOccurences());
    }
}
