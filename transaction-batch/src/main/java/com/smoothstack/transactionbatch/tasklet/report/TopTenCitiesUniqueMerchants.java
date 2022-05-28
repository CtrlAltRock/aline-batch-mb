package com.smoothstack.transactionbatch.tasklet.report;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.dto.outputdto.ReportBase;
import com.smoothstack.transactionbatch.report.ReportsContainer;
import com.thoughtworks.xstream.XStream;

public class TopTenCitiesUniqueMerchants {
    public static List<String> getReports(ReportsContainer reportsContainer) {
        XStream xStream = new XStream();

        xStream.alias("TopTenCityByUniqueMerchants", ReportBase.class);

        return generateReports(reportsContainer.getMerchantsByCityWithOnline())
            .map(n -> xStream.toXML(n))
            .collect(Collectors.toList());
    }


    public static Stream<ReportBase> generateReports(Stream<Map.Entry<String, Set<Long>>> metrics) {
        return metrics
            .sorted((n1, n2) -> Integer.compare(n2.getValue().size(), n1.getValue().size()))
            .limit(10)
            .map(n -> new ReportBase(n.getKey(), Integer.toString(n.getValue().size())));
    }
}
