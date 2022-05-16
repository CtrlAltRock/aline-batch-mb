package com.smoothstack.transactionbatch.tasklet.report;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.smoothstack.transactionbatch.dto.outputdto.UserErrorReport;
import com.smoothstack.transactionbatch.dto.outputdto.YearBy;
import com.smoothstack.transactionbatch.report.ReportsContainer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ErrorReportTests {
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

        

        assertEquals("66.6667%", report.getPercent());
    }

    @Test
    public void insufficientMultipleTest() {
        UserErrorReport report = InsufficientMultiple.generateReports(
            reportsContainer.getErrors(),
            BigDecimal.valueOf(reportsContainer.getUserCount())
        );

        assertEquals("33.3333%", report.getPercent());
    }

    @Test
    public void yearByFraudTest() {
        List<YearBy> frauds = FraudByYear.generateReports(reportsContainer.getFrauds())
            .collect(Collectors.toList());

        assertEquals(1, frauds.size());

        YearBy report = frauds.get(0);

        assertEquals(2002, report.getYear());
    }
}
