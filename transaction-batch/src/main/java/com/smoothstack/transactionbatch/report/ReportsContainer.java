package com.smoothstack.transactionbatch.report;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.dto.RecurringDto;
import com.smoothstack.transactionbatch.dto.TransactionTimeDto;
import com.smoothstack.transactionbatch.model.DepositBase;
import com.smoothstack.transactionbatch.model.ErrorBase;
import com.smoothstack.transactionbatch.model.TransactRead;

// Aggregate relevant information into streams here
public class ReportsContainer {
    private static ReportsContainer INSTANCE = null;

    private Merchants merchantInstance = new Merchants();

    private ErrorsFound errorsFound = new ErrorsFound();

    private Deposit depo = new Deposit();

    private LocationTransaction loca = new LocationTransaction();

    private TransactionType transactionType = new TransactionType();

    private TopTenTransaction topTenTransaction = new TopTenTransaction();

    // Report utils interface to clean up this class
    private final List<ReportUtils> reporters = new ArrayList<>();

    private ReportsContainer() { 
        reporters.add(merchantInstance);
        reporters.add(errorsFound);
        reporters.add(depo);
        reporters.add(loca);
        reporters.add(transactionType);
        reporters.add(topTenTransaction);
    }

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

    public void addItems(List<? extends TransactRead> items) {
        reporters.forEach(n -> n.addItems(items.parallelStream()));
    }

    public Collection<DepositBase> getDeposits() { return depo.getDeposits(); }

    public Stream<ErrorBase> getErrors() { return errorsFound.getErrors(); }

    public Stream<ErrorBase> getFrauds() { return errorsFound.getFrauds(); }

    public long getUserCount() { return errorsFound.getUserCount(); }

    public int getNumOfMerchants() { return merchantInstance.getNumOfMerchants(); }

    public AbstractMap<Integer, AtomicLong> getZipTransacts() { return loca.getZipTransacts(); }

    public AbstractMap<String, AtomicLong> getCityTransacts() { return loca.getCityTransacts(); }

    // Get just the recurring merchant information from each merchant
    public AbstractMap<RecurringDto, AtomicLong> getRecurring() {
        return merchantInstance.getTransactions();
    }

    public AbstractMap<String, AtomicLong> getTransactionTypes() { return transactionType.getTransactionTypes(); }

    public int getNumberOfTransactions() { return transactionType.getNumberOfTransactions(); }

    public Collection<TransactionTimeDto> getTopTen() { return topTenTransaction.getTopTen(); }

    public void clearCache() {
        reporters.forEach(n -> n.clearCache());
    }
}
