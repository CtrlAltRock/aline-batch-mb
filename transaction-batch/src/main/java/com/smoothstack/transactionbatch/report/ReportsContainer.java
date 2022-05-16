package com.smoothstack.transactionbatch.report;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.dto.LocationDto;
import com.smoothstack.transactionbatch.dto.RecurringDto;
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
            .map(n -> new ErrorBase(n.getUser(), n.getDate(), n.getErrors(), n.getFraud()));

        errorsFound.makeErrors(errors);
    }

    public void addMerchants(List<? extends TransactRead> merchants) { 
        Stream<Long> merch = merchants.stream()
            .map(n -> n.getMerchant());
        
        merchantInstance.addMerchants(merch);
    }

    public int getNumOfMerchants() { return merchantInstance.getNumOfMerchants(); }

    public AbstractMap<Integer, AtomicLong> getZipTransacts() { return loca.getZipTransacts(); }

    public AbstractMap<String, AtomicLong> getCityTransacts() { return loca.getCityTransacts(); }

    public void makeLocationTransacts(List<? extends TransactRead> items) {
        Stream<LocationDto> locations = items.parallelStream()
        // Filter out online transactions 
        .filter(n -> (!n.getZip().isBlank() && !n.getCity().isBlank()))
        .map(n -> {
            String oldZip = n.getZip();
            int zip = Integer.parseInt(oldZip.substring(0, oldZip.length() - 2));
            return new LocationDto(zip, n.getCity());
        });

        loca.makeTransactions(locations);
    }

    public AbstractMap<String, AtomicLong> getRecurring() { return merchantInstance.getTransactions(); }

    public void makeRecurringTransacts(List<? extends TransactRead> items) {
        Stream<RecurringDto> transacts = items.parallelStream()
            .map(n -> new RecurringDto(n.getMerchant(), n.getAmount(), n.getUser(), n.getCard()));

        merchantInstance.addTransactions(transacts);
    }

    public void clearCache() {
        errorsFound.clearMap();
        merchantInstance.clear();
        depo.clearMap();
        loca.clearMaps();
    }
}
