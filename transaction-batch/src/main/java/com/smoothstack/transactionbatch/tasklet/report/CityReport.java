package com.smoothstack.transactionbatch.tasklet.report;

import java.util.AbstractMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.dto.outputdto.LocationReport;
import com.smoothstack.transactionbatch.report.ReportsContainer;
import com.thoughtworks.xstream.XStream;

public class CityReport {
    public static List<String> getReports(ReportsContainer reportsContainer) {
        XStream xStream = new XStream();

        return generateReports(reportsContainer.getCityTransacts())
            .map(n -> xStream.toXML(n))
            .collect(Collectors.toList());
    }
    
    private static Stream<LocationReport> generateReports(AbstractMap<String, AtomicLong> transactions) {
        return transactions.entrySet().stream()
        .sorted((n1, n2) -> Long.compare(n2.getValue().get(), n1.getValue().get()))
        .limit(5)
        .map(n -> new LocationReport("City: " + n.getKey(), n.getValue().get()));
    }
}
