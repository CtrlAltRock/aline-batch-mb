package com.smoothstack.transactionbatch.tasklet.report;

import java.util.AbstractMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.outputdto.LocationReport;
import com.smoothstack.transactionbatch.report.ReportsContainer;
import com.thoughtworks.xstream.XStream;

public class ZipReporter {
    public static List<String> getReports(ReportsContainer reportsContainer) {
        XStream xStream = new XStream();

        return generateReports(reportsContainer.getZipTransacts())
            .map(n -> xStream.toXML(n))
            .collect(Collectors.toList());

    }

    // Return the top 5 zip codes by transaction amount
    private static Stream<LocationReport> generateReports(AbstractMap<Integer, AtomicLong> transactions) {
        return transactions.entrySet().stream()
        .sorted((n1, n2) -> Long.compare(n2.getValue().get(), n1.getValue().get()))
        .limit(5)
        .map(n -> new LocationReport("Zip: " + Integer.toString(n.getKey()), n.getValue().get()));
    }
}
