package com.smoothstack.transactionbatch.tasklet.report;

import java.util.AbstractMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.dto.RecurringDto;
import com.smoothstack.transactionbatch.dto.outputdto.Recurrences;
import com.smoothstack.transactionbatch.report.ReportsContainer;
import com.thoughtworks.xstream.XStream;

public class RecurringReport {
    public static List<String> getReports(ReportsContainer reportsContainer) {
        XStream xStream = new XStream();

        xStream.alias("Recurrences", Recurrences.class);

        return generateReports(reportsContainer.getRecurring())
            .map(n -> xStream.toXML(n))
            .collect(Collectors.toList());
    }

    public static Stream<Recurrences> generateReports(AbstractMap<RecurringDto, AtomicLong> transacts) {
        return transacts.entrySet().parallelStream()
            .sorted((n1, n2) -> Long.compare(n2.getValue().get(), n1.getValue().get()))
            .limit(5)
            .map(n -> {
                RecurringDto recur = n.getKey();
                return new Recurrences(
                    Long.toString(recur.getMerchantId()), 
                    "$" + recur.getAmount(), 
                    Long.toString(recur.getUserId()), 
                    Long.toString(recur.getCardId()), 
                    n.getValue().get()
                );
            });
    }
}
