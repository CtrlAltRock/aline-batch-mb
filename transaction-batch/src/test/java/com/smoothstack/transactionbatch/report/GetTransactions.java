package com.smoothstack.transactionbatch.report;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.model.TransactRead;

public class GetTransactions {
    private Stream<TransactRead> transactions;

    private static GetTransactions INSTANCE = null;

    public GetTransactions() {
        try {
            this.transactions = parseTransactions();
        } catch (IOException e) {
            assertTrue(false);
        }
    }

    public static GetTransactions getInstance() {
        if (INSTANCE == null) {
            synchronized (getInstance()) {
                if (INSTANCE == null) {
                    INSTANCE = new GetTransactions();
                }
            }
        }

        return INSTANCE;
    }

    public Stream<TransactRead> getTransactions() { return transactions; }

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
        String errors;
        
        if (!fields[13].isBlank()) errors = fields[13].substring(1, fields[13].length()); else errors = "";

        boolean isFraud = false;

        if (fields[14].equals("Yes")) isFraud = true;

        return new TransactRead(user, card, date, amount, use, merchant, city, state, zip, mcc, errors, isFraud);
    }

    public Stream<TransactRead> parseTransactions() throws IOException {
        final String basePath = new File("").getAbsolutePath();

        List<String> lines = Files.readAllLines(Paths.get(basePath, "/src/test/resources/TestData/test2.csv"));


        // Skip header line
        return lines.subList(1, lines.size()).stream()
            .map(n -> parse(n));
    }
}
