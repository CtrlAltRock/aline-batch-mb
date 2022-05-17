package com.smoothstack.transactionbatch.tasklet.report;

import com.smoothstack.transactionbatch.dto.outputdto.ReportBase;
import com.smoothstack.transactionbatch.report.ReportsContainer;
import com.thoughtworks.xstream.XStream;

public class TransactionTypeReport {
    public static String getReport(ReportsContainer reportsContainer) {
        XStream xStream = new XStream();

        xStream.alias("TransactionTypes", ReportBase.class);

        return xStream.toXML(
            generateReport(reportsContainer.getNumberOfTransactions())
        );

    }

    public static ReportBase generateReport(int numOfTypes) {
        return new ReportBase(
            "Number of Types of Transactions", 
            Integer.toString(numOfTypes)
        );
    }
}
