package com.smoothstack.transactionbatch.tasklet.report;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.dto.outputdto.UserErrorReport;
import com.smoothstack.transactionbatch.model.ErrorBase;
import com.smoothstack.transactionbatch.report.ReportsContainer;
import com.thoughtworks.xstream.XStream;

public class InsufficientOnce {
    public static String getReport(ReportsContainer reportsContainer) {
        XStream xStream = new XStream();

        xStream.alias("UserErrorReport", UserErrorReport.class);

        UserErrorReport report = generateReports(
            reportsContainer.getErrors(), 
            BigDecimal.valueOf(reportsContainer.getUserCount())
        );

        return xStream.toXML(report);        
    }

    public static UserErrorReport generateReports(Stream<ErrorBase> errors, BigDecimal userCount) {
        HashSet<Long> users = new HashSet<>();
        errors.filter(n -> (n.getErrorMessage().equals("Insufficient Balance"))).forEach(n -> users.add(n.getUserId()));

        BigDecimal perce = BigDecimal.valueOf(users.size())
            .divide(userCount, 6, RoundingMode.HALF_UP)
            .movePointRight(2);

        String percent = String.format("%s%%", perce);

        return new UserErrorReport("Percent of Users with Insufficient Balance at least once", percent);
    }
}
