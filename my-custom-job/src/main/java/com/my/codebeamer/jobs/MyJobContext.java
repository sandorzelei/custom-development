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

import java.util.Objects;

import org.quartz.JobKey;
import org.quartz.TriggerKey;

import com.intland.codebeamer.utils.scheduler.AbstractJobContext;

public class MyJobContext extends AbstractJobContext {

	private static final long serialVersionUID = 1L;

	private static final String JOB_NAME = "MyJob";

	private static final String GROUP_KEY = "MYGROUP";

	private String myAttribute;

	public MyJobContext(String myAttribute) {
		this.myAttribute = myAttribute;
	}

	@Override
	public JobKey createJobKey() {
		return new JobKey(getJobName(), GROUP_KEY);
	}

	@Override
	public TriggerKey createTriggerKey() {
		return new TriggerKey(getJobName(), GROUP_KEY);
	}

	private String getJobName() {
		return JOB_NAME;
	}

	@Override
	public int hashCode() {
		return Objects.hash(myAttribute);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyJobContext other = (MyJobContext) obj;
		return Objects.equals(myAttribute, other.myAttribute);
	}

	@Override
	public String toString() {
		return "MyJobContext [myAttribute=" + myAttribute + "]";
	}

}
