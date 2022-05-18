package com.smoothstack.transactionbatch.tasklet.report;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import com.smoothstack.transactionbatch.dto.TransactionTimeDto;
import com.smoothstack.transactionbatch.report.ReportsContainer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TopTenReportsTests {
    private ReportsContainer reportsContainer;

    @BeforeEach
    public void setUp() {
        reportsContainer = CreateReports.getInstance().getReports();
    }

    @Test
    public void topTenTest() {
        List<TransactionTimeDto> topTen = reportsContainer.getTopTen().stream().toList();

        assertEquals(10, topTen.size());

        assertEquals(new BigDecimal("1049.82"), topTen.get(0).getAmount());
    }
}
