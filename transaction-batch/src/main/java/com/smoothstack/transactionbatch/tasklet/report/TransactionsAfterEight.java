package com.smoothstack.transactionbatch.tasklet.report;

import java.util.AbstractMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.dto.outputdto.ReportBase;
import com.smoothstack.transactionbatch.report.ReportsContainer;
import com.thoughtworks.xstream.XStream;

public class TransactionsAfterEight {
    public static List<String> getReports(ReportsContainer reportsContainer) {
        XStream xStream = new XStream();

        xStream.alias("After8pmAndAbove$100", ReportBase.class);

        return generateReports(reportsContainer.getAfterEightPm())
            .map(n -> xStream.toXML(n))
            .collect(Collectors.toList());

    }

    public static Stream<ReportBase> generateReports(AbstractMap<String, AtomicLong> metrics) {
        return metrics.entrySet().parallelStream()
            .sorted((n1, n2) -> Long.compare(n2.getValue().get(), n1.getValue().get()))
            .limit(10)
            .map(n -> new ReportBase(n.getKey(), n.getValue().toString()));
    }
}
