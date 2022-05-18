package com.smoothstack.transactionbatch.tasklet.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.dto.TransactionTimeDto;
import com.smoothstack.transactionbatch.dto.outputdto.ReportBase;
import com.smoothstack.transactionbatch.report.ReportsContainer;
import com.thoughtworks.xstream.XStream;

public class TopTenReport {
    public static Collection<String> getReports(ReportsContainer reportsContainer) {
        XStream xStream = new XStream();

        xStream.alias("Top Ten Transactions", ReportBase.class);

        return generateReport(reportsContainer.getTopTen())
            .map(n -> xStream.toXML(n))
            .collect(Collectors.toList());
    }

    public static Stream<ReportBase> generateReport(Collection<TransactionTimeDto> topTen) {
        List<TransactionTimeDto> transactions = topTen.stream().collect(Collectors.toList());
        List<ReportBase>  reports = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            reports.add(new ReportBase(Integer.toString(i), "$" + transactions.get(i - 1).getAmount().toString()));
        }

        return reports.stream();
    }
}
