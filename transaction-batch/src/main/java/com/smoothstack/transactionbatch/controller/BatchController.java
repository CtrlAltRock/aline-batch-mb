package com.smoothstack.transactionbatch.controller;

import com.smoothstack.transactionbatch.config.UserBatchConfig;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
public class BatchController {
    
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private UserBatchConfig userBatchConfig;

    

    @GetMapping(path = "/user")
    public ResponseEntity<String> generateUsers() throws Exception {
        JobParametersBuilder params = new JobParametersBuilder();

        JobExecution exec = jobLauncher.run(userBatchConfig.userJob(), params.toJobParameters());

        return ResponseEntity.ok("");
    }

}
