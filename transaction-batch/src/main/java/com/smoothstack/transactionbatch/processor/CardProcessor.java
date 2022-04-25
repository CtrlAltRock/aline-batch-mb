package com.smoothstack.transactionbatch.processor;

import java.util.Optional;

import com.smoothstack.transactionbatch.generator.CardGenerator;
import com.smoothstack.transactionbatch.model.CardBase;
import com.smoothstack.transactionbatch.model.TransactRead;

import org.springframework.batch.item.ItemProcessor;

public class CardProcessor implements ItemProcessor<TransactRead, CardBase> {
    private CardGenerator cardGenerator = CardGenerator.getInstance();

    @Override
    public CardBase process(TransactRead item) throws Exception {
        Optional<CardBase> card = cardGenerator.generateCard(item.getUser(), item.getCard());

        if (card.isEmpty()) return null; else return card.get();
    }
}
