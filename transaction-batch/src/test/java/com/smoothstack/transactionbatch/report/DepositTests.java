package com.smoothstack.transactionbatch.report;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.smoothstack.transactionbatch.model.DepositBase;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class DepositTests {
    private Deposit depo;  

    @BeforeAll
    public void setUp() throws IOException {
        this.depo = new Deposit();

        // Populate deposits
        new GetTransactions().getTransactions()
        .forEach(n -> {
            BigDecimal amount = n.getAmount();
            if (amount.compareTo(BigDecimal.ZERO) == -1) {
                depo.makeDeposit(new DepositBase(n.getUser(), amount.abs()));
            }
        });
    }

    @Test
    public void depositTest() {
        List<DepositBase> deposits = depo.getDeposits().stream().collect(Collectors.toList());

        assertNotNull(deposits);
        assertEquals(1, deposits.size());
        assertEquals(new BigDecimal("13041.00"), deposits.get(0).getAmount());
    }
}
