package com.smoothstack.transactionbatch.tasklet.report;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

import com.smoothstack.transactionbatch.dto.outputdto.LocationReport;
import com.smoothstack.transactionbatch.report.ReportsContainer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LocationTests {
    private ReportsContainer reportsContainer;

    @BeforeEach
    public void setUp() {
        reportsContainer = CreateReports.getInstance().getReports();
    }

    @Test
    public void cityReportTests() {
        List<LocationReport> cityReports = 
            CityReport.generateReports(reportsContainer.getCityTransacts())
            .collect(Collectors.toList());

        assertEquals(5, cityReports.size());

        LocationReport location = cityReports.get(0);

        assertEquals("City: La Verne", location.getLocation());
    }

    @Test
    public void zipReportTests() {
        List<LocationReport> zipReports = 
            ZipReporter.generateReports(reportsContainer.getZipTransacts())
            .collect(Collectors.toList());

        assertEquals(5, zipReports.size());

        LocationReport location = zipReports.get(0);

        assertEquals("Zip: 91750", location.getLocation());
    }
}
