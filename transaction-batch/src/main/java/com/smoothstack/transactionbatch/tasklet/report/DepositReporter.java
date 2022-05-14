package com.smoothstack.transactionbatch.tasklet.report;

import java.util.Collection;
import java.util.stream.Collectors;

import com.smoothstack.transactionbatch.report.ReportsContainer;
import com.thoughtworks.xstream.XStream;

public class DepositReporter {
    public static Collection<String> getReports(ReportsContainer reportsContainer) {
        XStream xStream = new XStream();
        
        return reportsContainer.getDeposits().parallelStream()
            .sorted((n1, n2) -> Long.compare(n1.getId(), n2.getId()))
            .map(n -> xStream.toXML(n))
            .collect(Collectors.toList());
    }
}
