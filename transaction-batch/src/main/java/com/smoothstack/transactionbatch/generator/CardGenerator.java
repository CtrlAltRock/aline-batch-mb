package com.smoothstack.transactionbatch.generator;

import java.util.AbstractMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.smoothstack.transactionbatch.model.CardBase;
import com.vangogiel.luhnalgorithms.LuhnAlgorithms;

public class CardGenerator {
    private AbstractMap<Long, AbstractMap<Long, String>> cardList = new ConcurrentHashMap<>();
    private static AtomicLong cardCount = new AtomicLong(0);
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
            synchronized (CardGenerator.class) {
                if (!cardList.containsKey(user)) {
                    String cardNumber = luhnCard();

                    cardList.put(user, new ConcurrentHashMap<>());;

                    cardList.get(user).put(card, cardNumber);

                    return Optional.of( new CardBase(cardCount.incrementAndGet(), user, cardNumber) );
                } else if (!cardList.get(user).containsKey(card)) {
                    String cardNumber = luhnCard();

                    cardList.get(user).put(card, cardNumber);

                    return Optional.of( new CardBase(cardCount.incrementAndGet(), user, cardNumber) );
                }
            }
        }

        return Optional.empty(); 
    }

    public String luhnCard() {
        String cardNumber = Long.toString(LuhnAlgorithms.generateRandomLuhn(16));

        while (cardNumber.length() < 16) cardNumber = "0" + cardNumber;

        return cardNumber;
    }
}
