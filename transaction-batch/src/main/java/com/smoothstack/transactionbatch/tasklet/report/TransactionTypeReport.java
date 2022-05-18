package com.smoothstack.transactionbatch.tasklet.report;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.dto.outputdto.ReportBase;
import com.smoothstack.transactionbatch.report.ReportsContainer;
import com.thoughtworks.xstream.XStream;

public class TransactionTypeReport {
    public static Collection<String> getReport(ReportsContainer reportsContainer) {
        XStream xStream = new XStream();

        xStream.alias("TransactionType", ReportBase.class);

        return generateReport(reportsContainer.getTransactionTypes())
            .map(n -> xStream.toXML(n))
            .collect(Collectors.toList());

    }

    public static Stream<ReportBase> generateReport(AbstractMap<String, AtomicLong> numOfTypes) {
        return numOfTypes.entrySet().stream()
            .map(n -> new ReportBase(n.getKey(), n.getValue().toString()));
    }
}
