package com.smoothstack.transactionbatch.tasklet.report;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import com.smoothstack.transactionbatch.model.ErrorBase;
import com.smoothstack.transactionbatch.outputdto.UserErrorReport;
import com.smoothstack.transactionbatch.report.ErrorsFound;
import com.thoughtworks.xstream.XStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InsufficientOnce {
    public static void write(ErrorsFound errorsFound, String filePath) throws IOException {
        XStream xStream = new XStream();

        List<String> toWrite = new ArrayList<>();

        toWrite.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Report>");
 
        UserErrorReport errorReports = generateReports(errorsFound.getErrors(), errorsFound.getUserCount());

        toWrite.add(xStream.toXML(errorReports));

        toWrite.add("</Report>");

        Files.write(
            Paths.get(filePath, "output/reports/InsufficientOnceReport.xml"),
            toWrite,
            new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING}
        );        
    }

    private static UserErrorReport generateReports(Stream<ErrorBase> errors, long userCount) {
        HashSet<Long> users = new HashSet<>();
        errors.filter(n -> (n.getErrorMessage().equals("Insufficient Balance"))).forEach(n -> users.add(n.getUserId()));

        log.info("{} is divided by {}", users.size(), userCount);

        String percent = String.format("%1.3f%%", ((double) users.size() / userCount) * 100);

        return new UserErrorReport("Insufficient Balance at least once", percent);
    }
}
