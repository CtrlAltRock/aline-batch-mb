package com.smoothstack.transactionbatch.tasklet.report;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import com.smoothstack.transactionbatch.dto.outputdto.UserErrorReport;
import com.smoothstack.transactionbatch.report.ReportsContainer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InsufficientTester {
    private ReportsContainer reportsContainer;

    @BeforeEach
    public void setUp() {
        reportsContainer = CreateReports.getInstance().getReports();
    }

    @Test
    public void insufficientOnceTest() {
        UserErrorReport report = InsufficientOnce.generateReports(
            reportsContainer.getErrors(),
            BigDecimal.valueOf(reportsContainer.getUserCount())
        );

        

        assertEquals(report.getPercent(), "0.0000%");
    }

    @Test
    public void insufficientMultipleTest() {
        UserErrorReport report = InsufficientMultiple.generateReports(
            reportsContainer.getErrors(),
            BigDecimal.valueOf(reportsContainer.getUserCount())
        );

        assertEquals(report.getPercent(), "50.0000%");
    }
}
