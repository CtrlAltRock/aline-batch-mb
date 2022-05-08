package com.smoothstack.transactionbatch.tasklet.report;

import java.util.Collection;
import java.util.stream.Collectors;

import com.smoothstack.transactionbatch.report.ReportsContainer;
import com.thoughtworks.xstream.XStream;

public class DepositWriter {
    public static Collection<String> getReports(ReportsContainer reportsContainer) {
        XStream xStream = new XStream();
        
        return reportsContainer.getDeposits().parallelStream()
            .map(n -> xStream.toXML(n))
            .collect(Collectors.toList());
    }
}
