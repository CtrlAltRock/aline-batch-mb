package com.smoothstack.transactionbatch.controller;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
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
        JobExecution exec = jobLauncher.run(job, new JobParameters());

        if (exec.getExitStatus() != ExitStatus.COMPLETED) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exec.getExitStatus().toString());

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(exec.getExitStatus().toString());
    }
}
