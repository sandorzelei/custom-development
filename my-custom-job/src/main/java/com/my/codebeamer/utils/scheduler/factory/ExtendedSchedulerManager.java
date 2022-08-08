package com.my.codebeamer.utils.scheduler.factory;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.intland.codebeamer.persistence.dao.MonitoringDao;
import com.intland.codebeamer.utils.scheduler.SchedulerManager;
import com.intland.codebeamer.utils.scheduler.StopWatchJob;
import com.intland.codebeamer.utils.scheduler.factory.ParameterizedConcurrentSchedulerFactory;
import com.my.codebeamer.jobs.MyJob;

@Primary
@Service
public class ExtendedSchedulerManager extends SchedulerManager {
	
	private Scheduler myScheduler;

	@Autowired
	public ExtendedSchedulerManager(Scheduler instanceScheduler, Scheduler concurrentScheduler,
			Scheduler reportQuartzScheduler, Scheduler projectDeploymentExportScheduler, Scheduler escalationScheduler,
			Scheduler documentExportScheduler, Scheduler remoteJiraSyncScheduler, Scheduler remoteDoorsSyncScheduler,
			Scheduler externalProviderScmSyncScheduler, Scheduler monitoringScheduler,
			Scheduler monitoringConcurrentScheduler, Scheduler deleteProjectScheduler,
			ParameterizedConcurrentSchedulerFactory parameterizedConcurrentSchedulerFactory,
			ParameterizedConcurrentSchedulerFactory parameterizedMonitoringConcurrentSchedulerFactory,
			MonitoringDao monitoringDao, Scheduler computedUpdateScheduler, Scheduler workingSetScheduler,
			Scheduler workingSetUpdateScheduler, Scheduler backgroundJobScheduler, Scheduler backgroundInitJobScheduler,
			Scheduler backgroundJobExcelImportScheduler, Scheduler myScheduler) {
		super(instanceScheduler, concurrentScheduler, reportQuartzScheduler, projectDeploymentExportScheduler,
				escalationScheduler, documentExportScheduler, remoteJiraSyncScheduler, remoteDoorsSyncScheduler,
				externalProviderScmSyncScheduler, monitoringScheduler, monitoringConcurrentScheduler, deleteProjectScheduler,
				parameterizedConcurrentSchedulerFactory, parameterizedMonitoringConcurrentSchedulerFactory, monitoringDao,
				computedUpdateScheduler, workingSetScheduler, workingSetUpdateScheduler, backgroundJobScheduler,
				backgroundInitJobScheduler, backgroundJobExcelImportScheduler);
		this.myScheduler = myScheduler;
	}

	@Override
	public Scheduler getScheduler(Class<? extends StopWatchJob> jobClass) {
		
		if (MyJob.class.isAssignableFrom(jobClass)) {
			return myScheduler;
		}
		
		return super.getScheduler(jobClass);
	}
	
}
