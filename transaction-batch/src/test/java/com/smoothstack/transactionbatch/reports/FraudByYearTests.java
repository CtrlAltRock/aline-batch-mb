package com.smoothstack.transactionbatch.reports;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.smoothstack.transactionbatch.model.ErrorBase;
import com.smoothstack.transactionbatch.model.TransactRead;
import com.smoothstack.transactionbatch.report.ErrorsFound;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class FraudByYearTests {
    private List<TransactRead> transactions;

    private ErrorsFound errors;

    private TransactRead parse(String input) {
        String[] fields = input.split(",");

        long user = Long.parseLong(fields[0]);
        long card = Long.parseLong(fields[1]);

        String[] time = fields[5].split(":");
        int hour = Integer.valueOf(time[0]);
        int minute = Integer.valueOf(time[1]);

        LocalDateTime date = LocalDateTime.of(
            Integer.parseInt(fields[2]), 
            Integer.parseInt(fields[3]), 
            Integer.parseInt(fields[4]),
            hour,
            minute 
        );

        BigDecimal amount = new BigDecimal(fields[6].substring(1));
        String use = fields[7];
        long merchant = Long.parseLong(fields[8]);
        String city = fields[9];
        String state = fields[10];
        String zip = fields[11];
        int mcc = Integer.parseInt(fields[12]);
        String errors = fields[13];

        

        boolean isFraud = false;

        // Test data has extra comma inside error message
        if (!errors.isBlank()) {
            if (fields[15].equals("Yes")) isFraud = true;
        } else {
            if (fields[14].equals("Yes")) isFraud = true;
        }

        

        return new TransactRead(user, card, date, amount, use, merchant, city, state, zip, mcc, errors, isFraud);
    }

    @BeforeAll
    public void setUp() throws IOException {
        final String basePath = new File("").getAbsolutePath();

        List<String> lines = Files.readAllLines(Paths.get(basePath, "/input/test2.csv"));

        this.transactions = new ArrayList<TransactRead>(
            // Skip header line
            lines.subList(1, lines.size()).stream()
                .map(n -> parse(n))
                .collect(Collectors.toList())
        );

        this.errors = ErrorsFound.getInstance();
    }

    @Test
    public void loadTest() {
        TransactRead transaction = transactions.get(0);

        assertEquals(0, transaction.getUser());
        assertEquals(0, transaction.getCard());
        assertEquals(LocalDateTime.of(2002, 9, 1, 6, 21), transaction.getDate());
        assertEquals(new BigDecimal("134.09"), transaction.getAmount());
        assertEquals("Swipe Transaction", transaction.getUse());
        assertEquals(3527213246127876953L, transaction.getMerchant());
        assertEquals("La Verne", transaction.getCity());
        assertEquals("CA", transaction.getState());
        assertEquals("91750.0", transaction.getZip());
        assertTrue(transaction.getErrors().isBlank());
        assertTrue(!transaction.getFraud());
    }

    @Test
    public void depositTest() {
        // Populate errors
        transactions.forEach(n -> {
            if (!n.getErrors().isBlank() || n.getFraud()) {
                errors.makeError(n.getUser(), n.getDate(), n.getErrors(), n.getFraud());
            }
        });

        assertNotNull(errors.getErrorsFound());

        assertEquals(126, errors.getFrauds().size());

        ErrorBase fraud = new ArrayList<ErrorBase>(errors.getFrauds()).get(0);

        assertEquals(0, fraud.getUserId());
        assertEquals(LocalDateTime.of(2015, 11, 15, 12, 55), fraud.getTransactionTime());
        assertEquals("", fraud.getErrorMessage());
        assertTrue(fraud.isFraud());
    }
}
