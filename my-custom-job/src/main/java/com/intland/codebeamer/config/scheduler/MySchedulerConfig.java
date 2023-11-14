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

package com.intland.codebeamer.config.scheduler;

import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import com.intland.codebeamer.persistence.dao.MonitoringDao;
import com.intland.codebeamer.utils.scheduler.ExtendedScheduler;
import com.intland.codebeamer.utils.scheduler.ScheduleStarter;
import com.intland.codebeamer.utils.scheduler.SchedulerManager;
import com.intland.codebeamer.utils.scheduler.factory.ParameterizedConcurrentSchedulerFactory;
import com.intland.codebeamer.utils.scheduler.factory.SchedulerFactory;
import com.intland.codebeamer.utils.scheduler.factory.SchedulerPropertiesFactory.InstanceSchedulerPropertiesFactory;
import com.my.codebeamer.jobs.MyJobStarter;
import com.my.codebeamer.utils.scheduler.factory.ExtendedSchedulerManager;

@Configuration
@DependsOn({ "applicationContextHolder" })
public class MySchedulerConfig {

	@Bean
	public SchedulerFactory mySchedulerFactory() {
		return new SchedulerFactory("MyScheduler", new InstanceSchedulerPropertiesFactory());
	}

	@Bean
	public ExtendedScheduler myScheduler() {
		return mySchedulerFactory().getScheduler();
	}

	@Bean
	public MyJobStarter myJobStarter(ScheduleStarter scheduleStarter) {
		return new MyJobStarter(scheduleStarter);
	}

	@Bean
	@Primary
	public SchedulerManager schedulerManager(Scheduler instanceScheduler, Scheduler concurrentScheduler,
			Scheduler reportQuartzScheduler, Scheduler escalationScheduler, Scheduler documentExportScheduler,
			Scheduler remoteJiraSyncScheduler, Scheduler remoteDoorsSyncScheduler,
			Scheduler externalProviderScmSyncScheduler, Scheduler monitoringScheduler,
			Scheduler monitoringConcurrentScheduler, Scheduler deleteProjectScheduler,
			ParameterizedConcurrentSchedulerFactory parameterizedConcurrentSchedulerFactory,
			ParameterizedConcurrentSchedulerFactory parameterizedMonitoringConcurrentSchedulerFactory,
			MonitoringDao monitoringDao, Scheduler computedUpdateScheduler, Scheduler workingSetScheduler,
			Scheduler workingSetUpdateScheduler, Scheduler backgroundJobScheduler, Scheduler backgroundInitJobScheduler,
			Scheduler backgroundJobExcelImportScheduler,
			ParameterizedConcurrentSchedulerFactory parameterizedFlexLmLicenseAllocatorConcurrentSchedulerFactory,
			Scheduler flexLmLicenseAllocatorScheduler, Scheduler myScheduler) {
		return new ExtendedSchedulerManager(instanceScheduler, concurrentScheduler, reportQuartzScheduler,
				escalationScheduler, documentExportScheduler, remoteJiraSyncScheduler, remoteDoorsSyncScheduler,
				externalProviderScmSyncScheduler, monitoringScheduler, monitoringConcurrentScheduler,
				deleteProjectScheduler, parameterizedConcurrentSchedulerFactory,
				parameterizedMonitoringConcurrentSchedulerFactory, monitoringDao, computedUpdateScheduler,
				workingSetScheduler, workingSetUpdateScheduler, backgroundJobScheduler, backgroundInitJobScheduler,
				backgroundJobExcelImportScheduler, parameterizedFlexLmLicenseAllocatorConcurrentSchedulerFactory,
				flexLmLicenseAllocatorScheduler, myScheduler);
	}

}
