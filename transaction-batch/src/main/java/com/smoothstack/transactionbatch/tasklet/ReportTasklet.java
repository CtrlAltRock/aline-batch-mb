package com.smoothstack.transactionbatch.tasklet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import com.smoothstack.transactionbatch.report.ReportsContainer;
import com.smoothstack.transactionbatch.tasklet.report.BottomOnlineMonthly;
import com.smoothstack.transactionbatch.tasklet.report.CityReport;
import com.smoothstack.transactionbatch.tasklet.report.DepositReporter;
import com.smoothstack.transactionbatch.tasklet.report.FraudByYear;
import com.smoothstack.transactionbatch.tasklet.report.InsufficientMultiple;
import com.smoothstack.transactionbatch.tasklet.report.InsufficientOnce;
import com.smoothstack.transactionbatch.tasklet.report.RecurringReport;
import com.smoothstack.transactionbatch.tasklet.report.TopTenReport;
import com.smoothstack.transactionbatch.tasklet.report.TransactionTypeReport;
import com.smoothstack.transactionbatch.tasklet.report.UniqueMerchantsReport;
import com.smoothstack.transactionbatch.tasklet.report.ZipReporter;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class ReportTasklet implements Tasklet {
    private final String basePath = new File("").getAbsolutePath();

    private final ReportsContainer reportsContainer = ReportsContainer.getInstance();

    @Override
    public RepeatStatus execute(StepContribution contrib, ChunkContext cont) throws Exception {
        try {
            List<String> deposits = new ArrayList<>();

            deposits.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Deposits>");

            deposits.addAll(DepositReporter.getReports(reportsContainer));

            deposits.add("</Deposits>");

            fileWriter(basePath + "/output/reports/Deposits.xml", deposits);

            List<String> reports = new ArrayList<>();

            reports.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Reports>");

            reports.addAll(BottomOnlineMonthly.getReport(reportsContainer));

            reports.addAll(TopTenReport.getReports(reportsContainer));

            reports.addAll(TransactionTypeReport.getReport(reportsContainer));

            reports.addAll(RecurringReport.getReports(reportsContainer));

            reports.addAll(CityReport.getReports(reportsContainer));
            
            reports.addAll(ZipReporter.getReports(reportsContainer));

            reports.add(UniqueMerchantsReport.getReport(reportsContainer));

            reports.addAll(FraudByYear.getReports(reportsContainer));

            reports.add(InsufficientOnce.getReport(reportsContainer));

            reports.add(InsufficientMultiple.getReport(reportsContainer));

            reports.add("</Reports>");

            fileWriter(basePath + "/output/reports/MainReports.xml", reports);
            
        } finally {
            reportsContainer.clearCache();
        }

        return RepeatStatus.FINISHED;
    }

    public static void fileWriter(String filePath, List<String> toWrite) throws IOException {
        Files.write(
                Paths.get(filePath),
                toWrite,
                new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING}
            );
    }
}
