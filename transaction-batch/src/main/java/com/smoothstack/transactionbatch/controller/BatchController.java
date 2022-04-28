package com.smoothstack.transactionbatch.controller;

import java.time.Instant;
import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
public class BatchController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @GetMapping(path = "/load")
    public ResponseEntity<String> generateUsers() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder().addString("inputFile", "input/test.csv").addDate("time", Date.from(Instant.now())).toJobParameters();
        JobExecution exec = jobLauncher.run(job, jobParameters);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(exec.getExitStatus().toString());
    }
}
