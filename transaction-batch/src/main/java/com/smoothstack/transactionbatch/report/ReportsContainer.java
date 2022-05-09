package com.smoothstack.transactionbatch.report;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.model.DepositBase;
import com.smoothstack.transactionbatch.model.ErrorBase;
import com.smoothstack.transactionbatch.model.TransactRead;

// Aggregate information from writer in this class
public class ReportsContainer {
    private static ReportsContainer INSTANCE = null;

    private Merchants merchantInstance = new Merchants();

    private ErrorsFound errorsFound = new ErrorsFound();

    private Deposit depo = new Deposit();

    private ReportsContainer() { }

    public static ReportsContainer getInstance() {
        if (INSTANCE == null) {
            synchronized(ReportsContainer.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ReportsContainer();
                }
            }
        }
        return INSTANCE;
    }

    public Collection<DepositBase> getDeposits() { return depo.getDeposits(); }

    public void makeDeposits(List<? extends TransactRead> items) {
        depo.makeDeposits(items.parallelStream()
            .filter(n -> n.getAmount().compareTo(BigDecimal.ZERO) == -1)
            .map(n -> {
                return new DepositBase(n.getUser(), n.getAmount().abs());
            })
        );
    }

    public Stream<ErrorBase> getErrors() { return errorsFound.getErrors(); }

    public Stream<ErrorBase> getFrauds() { return errorsFound.getFrauds(); }

    public long getUserCount() { return errorsFound.getUserCount(); }

    public void makeErrors(List<? extends TransactRead> items) {
        Stream<ErrorBase> errors = items.parallelStream()
            .filter(n -> (!n.getErrors().isBlank() || n.getFraud()))
            .map(n -> new ErrorBase(n.getUser(), n.getDate(), n.getErrors(), n.getFraud()));

        errorsFound.makeErrors(errors);
    }

    public void addMerchants(List<? extends TransactRead> merchants) { 
        Stream<Long> merch = merchants.stream()
            .map(n -> n.getMerchant());
        
        merchantInstance.addMerchants(merch);
    }

    public int getNumOfMerchants() { return merchantInstance.getNumOfMerchants(); }

    public void clearCache() {
        errorsFound.clearMap();
        merchantInstance.clear();
        depo.clearMap();
    }
}
