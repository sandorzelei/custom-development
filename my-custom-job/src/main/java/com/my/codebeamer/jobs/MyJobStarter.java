package com.my.codebeamer.jobs;

import static org.quartz.DateBuilder.futureDate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DateBuilder;
import org.quartz.SchedulerException;

import com.intland.codebeamer.utils.scheduler.ScheduleStarter;

public class MyJobStarter {

	private static final Logger logger = LogManager.getLogger(MyJobStarter.class);

    private ScheduleStarter scheduleStarter;

    public MyJobStarter(ScheduleStarter scheduleStarter) {
        this.scheduleStarter = scheduleStarter;
    }

    @PostConstruct
    public void scheduleMyJob() throws SchedulerException {
    	logger.info("Schedule my jobs....");
        long periodInSecond = TimeUnit.SECONDS.toSeconds(5);
        Date startAt = futureDate(1, DateBuilder.IntervalUnit.MINUTE);
        this.scheduleStarter.scheduleJob(MyJob.class, startAt, 0, periodInSecond, new MyJobContext("MyAttributeValue"));
    }

}
