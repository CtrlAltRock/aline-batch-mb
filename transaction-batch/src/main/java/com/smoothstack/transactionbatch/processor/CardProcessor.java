package com.smoothstack.transactionbatch.processor;


import com.smoothstack.transactionbatch.generator.CardGenerator;
import com.smoothstack.transactionbatch.model.TransactRead;

import org.springframework.batch.item.ItemProcessor;

public class CardProcessor implements ItemProcessor<TransactRead, TransactRead> {
    private static CardGenerator cardGenerator = CardGenerator.getInstance();

    @Override
    public TransactRead process(TransactRead item) throws Exception {
        cardGenerator.generateCard(item.getUser(), item.getCard());

        return item;
    }
}
