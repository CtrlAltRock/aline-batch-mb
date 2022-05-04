package com.smoothstack.transactionbatch.tasklet.report;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.model.ErrorBase;
import com.smoothstack.transactionbatch.outputdto.YearBy;
import com.smoothstack.transactionbatch.report.ErrorsFound;
import com.thoughtworks.xstream.XStream;

public class FraudByYear {
    public static void write(String filePath) throws IOException {
        XStream xStream = new XStream();

        ErrorsFound errorsFound = ErrorsFound.getInstance();
        
        List<String> toWrite = new ArrayList<>();
 
        toWrite.addAll(Arrays.asList("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<Report>"));
 
        List<String> errorReports = generateReports(errorsFound.getFrauds())
            .sorted((n1, n2) -> n1.getYear() < n2.getYear() ? -1 : 1)
            .map((n) -> xStream.toXML(n))
            .collect(Collectors.toList());

        toWrite.addAll(errorReports);

        toWrite.add("</Report>");

        Files.write(
            Paths.get(filePath, "output/reports/YearByFraudReport.xml"),
            toWrite,
            new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING}
        );
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
                return new YearBy(n.getKey(), percent);
            });
    }
}
