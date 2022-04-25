package com.smoothstack.transactionbatch.generator;

import java.util.AbstractMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.smoothstack.transactionbatch.model.CardBase;
import com.vangogiel.luhnalgorithms.LuhnAlgorithms;

public class CardGenerator {
    private AbstractMap<Long, AbstractMap<Long, String>> cardList = new ConcurrentHashMap<>();
    private static CardGenerator INSTANCE = null;

    private CardGenerator() {}

    public static CardGenerator getInstance() {
        if (INSTANCE == null) {
            synchronized(CardGenerator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CardGenerator();
                }
            }
        }
        return INSTANCE;
    }

    public Optional<CardBase> generateCard(long user, long card) {
        if (!cardList.containsKey(user) || !cardList.get(user).containsKey(card)) {
            synchronized (this) {
                if (!cardList.containsKey(user) || !cardList.get(user).containsKey(card)) {
                    String cardNumber = Long.toString(LuhnAlgorithms.generateRandomLuhn(16));

                    CardBase newCard = new CardBase(user, card, cardNumber);

                    ConcurrentHashMap<Long, String> createdCard = new ConcurrentHashMap<>();
                    createdCard.put(card, cardNumber);

                    cardList.put(user, createdCard);

                    return Optional.of(newCard);
                }    
            }
        }

        return Optional.empty(); 
    }

}
