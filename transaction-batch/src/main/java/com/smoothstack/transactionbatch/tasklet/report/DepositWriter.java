package com.smoothstack.transactionbatch.tasklet.report;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.smoothstack.transactionbatch.report.Deposit;
import com.thoughtworks.xstream.XStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DepositWriter {
    public static void write(String filePath) throws IOException {
        XStream xStream = new XStream();

        final Deposit depo = Deposit.getInstance();
        
        List<String> toWrite = new ArrayList<>();
 
        toWrite.addAll(Arrays.asList("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<Deposits>"));
 
        List<String> accounts = depo.getBalances().values().stream()
            .map((n) -> xStream.toXML(n))
            .collect(Collectors.toList());

        toWrite.addAll(accounts);
        toWrite.add("</Deposits>");

        Files.write(
            Paths.get(filePath, "output/reports/DepositReport.xml"),
            toWrite,
            new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING}
        );
    }
}
