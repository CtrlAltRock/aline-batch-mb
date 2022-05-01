package com.smoothstack.transactionbatch.report;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.concurrent.ConcurrentHashMap;

import com.smoothstack.transactionbatch.model.DepositBase;

public class Deposit {
    // Atomic reference assures atomicity when balance is updated
    private AbstractMap<Long, DepositBase> accountBalance = new ConcurrentHashMap<>();
    private static Deposit INSTANCE = null;

    private Deposit() {}

    public static Deposit getInstance() {
        if (INSTANCE == null) {
            synchronized(Deposit.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Deposit();
                }
            }
        }
        return INSTANCE;
    }

    public AbstractMap<Long, DepositBase> getBalances() { return accountBalance; }

    public void makeTransaction(long user, BigDecimal amount) {
        if (!accountBalance.containsKey(user)) {
            synchronized (this) {
                if (!accountBalance.containsKey(user)) {
                    // Scale set to two for financial transactions
                    accountBalance.put(user, new DepositBase());
                }
            }
        }
        
        synchronized (this) {
            DepositBase currentBalance = accountBalance.get(user);
            currentBalance.setCurrentBalance(amount.add(currentBalance.getCurrentBalance()));
        }
    }

}
