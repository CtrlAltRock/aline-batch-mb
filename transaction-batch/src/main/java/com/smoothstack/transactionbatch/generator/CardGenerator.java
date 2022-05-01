package com.smoothstack.transactionbatch.generator;

import java.util.AbstractMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.smoothstack.transactionbatch.model.CardBase;
import com.vangogiel.luhnalgorithms.LuhnAlgorithms;

public class CardGenerator {
    private AbstractMap<Long, AbstractMap<Long, CardBase>> cardList = new ConcurrentHashMap<>();
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

    public AbstractMap<Long, AbstractMap<Long, CardBase>> getContext() {return cardList;}

    public Optional<CardBase> generateCard(long user, long card) {        
        if (!cardList.containsKey(user)) {
            synchronized (CardGenerator.class) {
                if (!cardList.containsKey(user)) cardList.put(user, new ConcurrentHashMap<>());
            }
        }
        if (!cardList.get(user).containsKey(card)) {
            synchronized (CardGenerator.class) {
                if (!cardList.get(user).containsKey(card)) {
                    CardBase car = new CardBase(cardCount.incrementAndGet(), user, luhnCard());

                    cardList.get(user).put(card, car);

                    return Optional.of(car);
                }
            }
        }
        
        return Optional.empty();
    }

    // Uses Luhn Algorithm to generate card numbers
    public String luhnCard() {
        String cardNumber = Long.toString(LuhnAlgorithms.generateRandomLuhn(16));

        while (cardNumber.length() < 16) cardNumber = "0" + cardNumber;

        return cardNumber;
    }
}
