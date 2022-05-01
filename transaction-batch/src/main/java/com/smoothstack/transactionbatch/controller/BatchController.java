package com.smoothstack.transactionbatch.controller;

import java.time.Instant;
import java.util.Date;

import com.smoothstack.transactionbatch.dto.GeneratorRequest;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
public class BatchController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @PostMapping(path = "/load", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> generateUsers(@RequestBody GeneratorRequest req) throws Exception {
        JobParametersBuilder jobParameters = new JobParametersBuilder()
            .addString("inputFile", "input/card_transaction.v1.csv").addDate("time", Date.from(Instant.now()));


        // Allow to not kick off data generation based on form passed in post data
        if (req.getDataEnrich() != null) jobParameters.addString("enrich", req.getDataEnrich().toLowerCase());

        if (req.getDataAnalyze() != null) jobParameters.addString("analyze", req.getDataAnalyze().toLowerCase());

        JobExecution exec = jobLauncher.run(job, jobParameters.toJobParameters());

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(exec.getExitStatus().getExitCode());
    }
}
