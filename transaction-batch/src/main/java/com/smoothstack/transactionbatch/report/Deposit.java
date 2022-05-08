package com.smoothstack.transactionbatch.report;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.model.DepositBase;

public class Deposit {
    private AbstractMap<Long, DepositBase> accountBalance = new ConcurrentHashMap<>();

    public Collection<DepositBase> getDeposits() { return accountBalance.values(); }

    public void makeDeposit(DepositBase deposit) {
        long user = deposit.getId();

        if (!accountBalance.containsKey(user)) {
            synchronized (this) {
                if (!accountBalance.containsKey(user)) {
                    // Scale set to two for financial transactions
                    accountBalance.put(user, deposit);
                }
            }
        } else {
            synchronized (this) {
                DepositBase currentBalance = accountBalance.get(user);
                currentBalance.setAmount(
                    deposit.getAmount().add(currentBalance.getAmount())
                );
            }
        }       
    }

    public void makeDeposits(Stream<DepositBase> deposits) {
        deposits.forEach(n -> makeDeposit(n));
    }

    // Clean up after writing
    public void clearMap() {
        accountBalance.clear();
    }

}
