package com.smoothstack.transactionbatch.tasklet.report;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.model.ErrorBase;
import com.smoothstack.transactionbatch.outputdto.YearBy;
import com.smoothstack.transactionbatch.report.ReportsContainer;
import com.thoughtworks.xstream.XStream;

public class FraudByYear {
    public static Collection<String> getReports(ReportsContainer reportsContainer) {
        XStream xStream = new XStream();
        
        return generateReports(reportsContainer.getFrauds())
            .map(n -> xStream.toXML(n))
            .collect(Collectors.toList());
    }

    // Return stream to save collecting until we convert to xml
    private static Stream<YearBy> generateReports(Stream<ErrorBase> errors) {
        final AbstractMap<Integer, Integer> fraudByYear = new HashMap<>();

        // Increment for each fraud report per year
        errors.forEach((n) -> {
            int year = n.getTransactionTime().getYear();
            if (fraudByYear.containsKey(year)) {
                fraudByYear.put(year, fraudByYear.get(year) + 1);
            } else {
                fraudByYear.put(year, 1);
            }
        });

        double totalFrauds = (double) fraudByYear.values().stream().mapToInt(Integer::intValue).sum();

        return fraudByYear.entrySet().stream()
            .map((Map.Entry<Integer, Integer> n) -> {
                String percent = String.format("%1.3f%%", (n.getValue() / totalFrauds) * 100);
                return new YearBy("Fraud percent by Year", n.getKey(), percent);
            });
    }
}
