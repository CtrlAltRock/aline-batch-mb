package com.smoothstack.transactionbatch.tasklet.report;

import com.smoothstack.transactionbatch.outputdto.ReportBase;
import com.smoothstack.transactionbatch.report.ReportsContainer;
import com.thoughtworks.xstream.XStream;

public class UniqueMerchantsReport {
    public static String getReport(ReportsContainer reports) {
        XStream xStream = new XStream();

        ReportBase report = new ReportBase("Unique Merchants", Integer.toString(reports.getNumOfMerchants()));

        return xStream.toXML(report);
    }
}
