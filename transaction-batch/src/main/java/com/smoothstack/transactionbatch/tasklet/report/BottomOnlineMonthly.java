package com.smoothstack.transactionbatch.tasklet.report;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.dto.OnlineMonthlyDto;
import com.smoothstack.transactionbatch.dto.outputdto.ReportBase;
import com.smoothstack.transactionbatch.report.ReportsContainer;
import com.thoughtworks.xstream.XStream;

public class BottomOnlineMonthly {
    public static Collection<String> getReport(ReportsContainer reportsContainer) {
        XStream xStream = new XStream();

        xStream.alias("Bottom5OnlineTransactionsbyMonth", ReportBase.class);

        return generateReport(reportsContainer.getMonthlyTransact())
            .map(n -> xStream.toXML(n))
            .collect(Collectors.toList());
    }
    
    public static Stream<ReportBase> generateReport(AbstractMap<OnlineMonthlyDto, AtomicLong> monthlyTransacts) {
        return monthlyTransacts.entrySet().stream()
            .sorted((n1, n2) -> Long.compare(n1.getValue().get(), n2.getValue().get()))
            .limit(5)
            .map(n -> new ReportBase(n.getKey().getMonth().toString(), n.getValue().toString()));
    }
}
