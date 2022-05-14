package com.smoothstack.transactionbatch.tasklet.report;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.dto.outputdto.YearBy;
import com.smoothstack.transactionbatch.model.ErrorBase;
import com.smoothstack.transactionbatch.report.ReportsContainer;
import com.thoughtworks.xstream.XStream;

public class FraudByYear {
    public static Collection<String> getReports(ReportsContainer reportsContainer) {
        XStream xStream = new XStream();
        
        return generateReports(reportsContainer.getFrauds())
            .sorted((n1, n2) -> Integer.compare(n1.getYear(), n2.getYear()))
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

        BigDecimal totalFrauds = BigDecimal.valueOf(fraudByYear.values().stream().mapToInt(Integer::intValue).sum());

        return fraudByYear.entrySet().stream()
            .map((Map.Entry<Integer, Integer> n) -> {
                String percent = String.format(
                    "%s%%",
                    BigDecimal.valueOf(n.getValue())
                    .divide(totalFrauds, 6, RoundingMode.HALF_UP)
                    .movePointRight(2)
                    .toPlainString()
                );
                return new YearBy("Fraud percent by Year", n.getKey(), percent);
            });
    }
}
