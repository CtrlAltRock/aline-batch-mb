package com.smoothstack.transactionbatch.controller;

//import com.smoothstack.transactionbatch.config.CardBatchConfig;
import com.smoothstack.transactionbatch.config.UserGenBatchConfig;

import org.springframework.batch.core.ExitStatus;
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

//@RestController
@RequestMapping("/batch")
public class BatchController {
    
    @Autowired
    private JobLauncher jobLauncher;

    //@Autowired
    //private UserGenBatchConfig userBatchConfig;

    //@Autowired
    //private CardBatchConfig cardBatchConfig;

    private JobParameters params = new JobParametersBuilder().toJobParameters();

    //@GetMapping(path = "/user")
    //public ResponseEntity<String> generateUsers() throws Exception {
    //    JobExecution exec = jobLauncher.run(userBatchConfig.userJob(), params);
//
    //    if (exec.getExitStatus() != ExitStatus.COMPLETED) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exec.getExitStatus().toString());
//
    //    return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(exec.getExitStatus().toString());
    //}

    //@GetMapping(path = "/card")
    //public ResponseEntity<String> generateCards() throws Exception {
    //    JobExecution exec = jobLauncher.run(cardBatchConfig.cardJob(), params);
//
    //    if (exec.getExitStatus() != ExitStatus.COMPLETED) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exec.getExitStatus().toString());
//
    //    return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(exec.getExitStatus().toString());
    //}

}
