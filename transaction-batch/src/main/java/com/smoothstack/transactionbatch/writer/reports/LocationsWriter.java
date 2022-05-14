package com.smoothstack.transactionbatch.writer.reports;

import java.util.List;

import com.smoothstack.transactionbatch.model.TransactRead;
import com.smoothstack.transactionbatch.report.ReportsContainer;

import org.springframework.batch.item.ItemWriter;

public class LocationsWriter implements ItemWriter<TransactRead>{
    private final ReportsContainer reportsContainer = ReportsContainer.getInstance();

    @Override
    public void write(List<? extends TransactRead> items) {
        reportsContainer.makeLocationTransacts(items);
    }
}
