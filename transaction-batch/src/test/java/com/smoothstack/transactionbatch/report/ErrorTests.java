package com.smoothstack.transactionbatch.report;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.smoothstack.transactionbatch.model.ErrorBase;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class ErrorTests {
    private ErrorsFound errors;

    @BeforeAll
    public void setUp() throws IOException {
        this.errors = new ErrorsFound();

        // Populate errors
        errors.addItems(new GetTransactions().getTransactions());
    }


    @Test
    public void fraudsTest() {
        assertNotNull(errors.getErrorsFound());

        List<ErrorBase> frauds = errors.getFrauds().collect(Collectors.toList());

        assertEquals(2, frauds.size());

        ErrorBase fraud = frauds.get(0);

        assertNotNull(fraud.getTransactionTime());

        assertTrue(fraud.isFraud());
    }

    @Test
    public void errorsTest() {
        List<ErrorBase> error = this.errors.getErrors().collect(Collectors.toList());

        assertEquals(71, error.size());

        ErrorBase testError = error.get(0);

        assertEquals("Technical Glitch", testError.getErrorMessage());
    }
}

