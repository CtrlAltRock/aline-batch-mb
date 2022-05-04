package com.smoothstack.transactionbatch.tasklet.report;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.model.ErrorBase;
import com.smoothstack.transactionbatch.outputdto.UserErrorReport;
import com.smoothstack.transactionbatch.report.ErrorsFound;
import com.thoughtworks.xstream.XStream;

public class InsufficientOnce {
    public static void write(String filePath) throws IOException {
        XStream xStream = new XStream();

        ErrorsFound errorsFound = ErrorsFound.getInstance();

        List<String> toWrite = new ArrayList<>();

        toWrite.addAll(Arrays.asList("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<Report>"));
 
        Collection<String> errorReports = generateReports(errorsFound.getErrors())
            .map(n -> xStream.toXML(n))
            .collect(Collectors.toList());

        toWrite.addAll(errorReports);

        toWrite.add("</Report>");

        Files.write(
            Paths.get(filePath, "output/reports/InsufficientOnceReport.xml"),
            toWrite,
            new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING}
        );        
    }

    private static Stream<UserErrorReport> generateReports(Stream<ErrorBase> errors) {
        HashSet<Long> users = new HashSet<>();
        errors.filter(n -> (n.getErrorMessage().equals("Insufficient Balance"))).forEach(n -> users.add(n.getUserId()));

        return users.stream().map(n -> new UserErrorReport(n, "Insufficient Balance"));
    }
}
