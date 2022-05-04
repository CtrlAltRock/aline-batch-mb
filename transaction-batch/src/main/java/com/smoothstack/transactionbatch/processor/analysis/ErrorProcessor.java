package com.smoothstack.transactionbatch.processor.analysis;

import com.smoothstack.transactionbatch.model.TransactRead;
import com.smoothstack.transactionbatch.report.ErrorsFound;

import org.springframework.batch.item.ItemProcessor;

public class ErrorProcessor implements ItemProcessor<TransactRead, TransactRead> {
    private static ErrorsFound errorsFound = ErrorsFound.getInstance();

    @Override
    public TransactRead process(TransactRead item) {
        errorsFound.makeError(item.getUser(), item.getDate(), item.getErrors(), item.getFraud());
        

        return item;
    }
}
