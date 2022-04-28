package com.smoothstack.transactionbatch.reader;

import com.smoothstack.transactionbatch.mapper.CustomFieldSetMapper;
import com.smoothstack.transactionbatch.model.TransactRead;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class TransactionReader {
    public ItemReader<TransactRead> read() {        
        return new FlatFileItemReaderBuilder<TransactRead>()
                .linesToSkip(1)
                .name("csvflatfileitemreader")
                .resource(new FileSystemResource("input/test2.csv"))
                .delimited()
                .names(new String[]{"user", "card", "year", "month", "day", "time", "amount", "use", "merchant", "city", "state", "zip", "mcc", "errors", "isFraud"})
                .fieldSetMapper(new CustomFieldSetMapper())
                .build();
    }
}
