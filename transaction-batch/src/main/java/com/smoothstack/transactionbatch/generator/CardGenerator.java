package com.smoothstack.transactionbatch.generator;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.smoothstack.transactionbatch.model.CardBase;
import com.vangogiel.luhnalgorithms.LuhnAlgorithms;

public class CardGenerator {
    private AbstractMap<Long, Set<Long>> cardList = new ConcurrentHashMap<>();
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

    public AbstractMap<Long, Set<Long>> getContext() {return cardList;}

    public Optional<CardBase> generateCard(long user, long card) {        
        if (!cardList.containsKey(user)) {
            synchronized (CardGenerator.class) {
                if (!cardList.containsKey(user)) cardList.put(user, new HashSet<>());
            }
        }
        if (!cardList.get(user).contains(card)) {
            synchronized (CardGenerator.class) {
                if (!cardList.get(user).contains(card)) {
                    CardBase car = new CardBase(cardCount.incrementAndGet(), user, luhnCard());

                    cardList.get(user).add(card);

                    return Optional.of(car);
                }
            }
        }
        
        return Optional.empty();
    }

    // Uses Luhn Algorithm to generate card numbers
    public String luhnCard() {
        long cardNumber = LuhnAlgorithms.generateRandomLuhn(16);

        // If number has too few digits, left pad with 0's
        return String.format("%016d", cardNumber);
    }

    // Clean up after writing
    public void clearMap() {
        cardList.clear();
    }
}
