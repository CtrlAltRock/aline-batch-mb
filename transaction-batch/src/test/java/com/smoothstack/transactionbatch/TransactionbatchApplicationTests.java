package com.smoothstack.transactionbatch;

import com.smoothstack.transactionbatch.config.AsyncConfig;
import com.smoothstack.transactionbatch.config.CompositeBatch;
import com.smoothstack.transactionbatch.generator.CardGenerator;
import com.smoothstack.transactionbatch.generator.MerchantGenerator;
import com.smoothstack.transactionbatch.generator.UserGenerator;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.time.Instant;
import java.util.Date;

@SpringBatchTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {CompositeBatch.class, AsyncConfig.class})
@TestInstance(Lifecycle.PER_CLASS)
class TransactionbatchApplicationTests {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private JobRepositoryTestUtils jobRepositoryTestUtils;

	@AfterAll
	public void cleanUp() {
		UserGenerator.getInstance().clearMap();
		CardGenerator.getInstance().clearMap();
		MerchantGenerator.getInstance().clearMap();
		jobRepositoryTestUtils.removeJobExecutions();
	}

	private JobParameters jobParameters() {
		return new JobParametersBuilder()
			.addDate("time", Date.from(Instant.now()))
			.addString("inputFile", "/home/artixulate/vscode/coffee/aline-financial/transactionbatch/transaction-batch/src/test/resources/TestData/test2.csv")
			.addString("enrich", "true")
			.addString("analyze", "true")
			.toJobParameters();
	}

	@Test
	public void ifLaunchedThenSuccess() throws Exception {
		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters());
		JobInstance actualJobInstance = jobExecution.getJobInstance();
		ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualJobInstance.getJobName(), is("Composite Batch"));
		assertThat(actualJobExitStatus.getExitCode(), is("COMPLETED"));
	}

}
