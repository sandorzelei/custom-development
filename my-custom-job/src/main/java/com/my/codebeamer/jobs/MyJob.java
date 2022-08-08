/*
 * Copyright by Intland Software
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Intland Software. ("Confidential Information"). You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Intland.
 */

package com.my.codebeamer.jobs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

import com.intland.codebeamer.utils.scheduler.JobStoppingException;
import com.intland.codebeamer.utils.scheduler.StopWatchJob;

@PersistJobDataAfterExecution
public class MyJob extends StopWatchJob {

	private static final Logger logger = LogManager.getLogger(MyJob.class);

	private MyJobContext jobContext;
	
	public MyJob() {
		super();
	}

	@Override
	public void doExecute(JobExecutionContext context) throws JobExecutionException {
		try {
			logger.info("Running my custom jobs..... {}", jobContext);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new JobStoppingException(e);
		}
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	public void setJobContext(MyJobContext jobContext) {
		this.jobContext = jobContext;
	}
	
}
