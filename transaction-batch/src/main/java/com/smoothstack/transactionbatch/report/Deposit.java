package com.smoothstack.transactionbatch.report;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.model.DepositBase;
import com.smoothstack.transactionbatch.model.TransactRead;

public class Deposit implements ReportUtils {
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

    @Override
    public void addItems(Stream<? extends TransactRead> items) {
        items.filter(n -> n.getAmount().compareTo(BigDecimal.ZERO) == -1)
        .map(n -> new DepositBase(n.getUser(), n.getAmount().abs()))
        .forEach(this::makeDeposit);;
    }


    @Override
    public void clearCache() {
        accountBalance.clear();
    }

}
