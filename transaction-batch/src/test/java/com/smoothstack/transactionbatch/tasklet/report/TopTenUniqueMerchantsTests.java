package com.smoothstack.transactionbatch.tasklet.report;

import com.smoothstack.transactionbatch.dto.outputdto.ReportBase;
import com.smoothstack.transactionbatch.report.ReportsContainer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TopTenUniqueMerchantsTests {
    private ReportsContainer reportsContainer;

    @BeforeEach
    public void setUp() {
        reportsContainer = CreateReports.getInstance().getReports();
    }

    @Test
    public void topTenUniqueMerchantsTest() {
        List<ReportBase> reports = TopTenCitiesUniqueMerchants.generateReports(reportsContainer.getMerchantsByCityWithOnline()).collect(Collectors.toList());

        Set<Long> onlineMerchants = reportsContainer.getOnlineMerchants();

        assertEquals(95, reportsContainer.getMerchantByCity().size());

        assertEquals(23, onlineMerchants.size());

        assertEquals(1, reports.size());

        ReportBase testReport = reports.get(0);

        assertEquals("Florence", testReport.getTitle());
        assertEquals("3", testReport.getReport());
    }
}
