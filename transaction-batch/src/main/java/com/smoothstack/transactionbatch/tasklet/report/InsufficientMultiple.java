package com.smoothstack.transactionbatch.tasklet.report;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.model.ErrorBase;
import com.smoothstack.transactionbatch.outputdto.UserErrorReport;
import com.smoothstack.transactionbatch.report.ReportsContainer;
import com.thoughtworks.xstream.XStream;

public class InsufficientMultiple {
    public static String getReport(ReportsContainer reportsContainer) {
        XStream xStream = new XStream();

        UserErrorReport report = generateReports(
            reportsContainer.getErrors(), 
            BigDecimal.valueOf(reportsContainer.getUserCount())
        );

        return xStream.toXML(report);       
    }

    private static UserErrorReport generateReports(Stream<ErrorBase> errors, BigDecimal userCount) {
        HashMap<Long, Boolean> users = new HashMap<>();
        errors.filter(n -> (n.getErrorMessage().equals("Insufficient Balance")))
            .forEach(n -> {
                if (users.containsKey(n.getUserId())) {
                    users.put(n.getUserId(), true);
                } else {
                    users.put(n.getUserId(), false);
                }
            });

        BigDecimal repeatOffenders = BigDecimal.valueOf(users.values().stream()
            .filter(n -> n)
            .collect(Collectors.toList())
            .size()
        );

        String percent = String.format("%s%%", repeatOffenders.divide(userCount, 6, RoundingMode.HALF_UP)
            // Convert decimal to percent value
            .movePointRight(2)
            .toPlainString()
        );

        return new UserErrorReport("Insufficient Balance more than once", percent);
    }
}
