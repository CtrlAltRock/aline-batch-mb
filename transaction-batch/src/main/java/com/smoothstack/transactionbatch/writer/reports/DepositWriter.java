package com.smoothstack.transactionbatch.writer.reports;

import java.util.List;

import com.smoothstack.transactionbatch.model.TransactRead;
import com.smoothstack.transactionbatch.report.ReportsContainer;

import org.springframework.batch.item.ItemWriter;

public class DepositWriter implements ItemWriter<TransactRead> {
    private final ReportsContainer reportsContainer = ReportsContainer.getInstance();
    
    @Override
    public void write(List<? extends TransactRead> items) {
        reportsContainer.makeDeposits(items);
    }
}