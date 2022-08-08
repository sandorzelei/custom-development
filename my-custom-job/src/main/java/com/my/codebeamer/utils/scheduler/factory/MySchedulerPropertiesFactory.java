package com.my.codebeamer.utils.scheduler.factory;

import static com.intland.codebeamer.utils.EnvironmentUtils.getConfiguration;
import static org.quartz.impl.StdSchedulerFactory.PROP_THREAD_POOL_PREFIX;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.intland.codebeamer.utils.scheduler.factory.SchedulerPropertiesFactory;

public class MySchedulerPropertiesFactory extends SchedulerPropertiesFactory {

	private static final String THREAD_COUNT_PROPERTY_NAME = concatProps(PROP_THREAD_POOL_PREFIX, "threadCount");
	
	private static final String THREAD_COUNT = "CB_SCHEDULER_FACTORY_THREAD_COUNT_MY";

	protected Map<MyEnvironmentVariableConfig, String> configurationToPropertiesMap = new HashMap<>();

	public MySchedulerPropertiesFactory() {
		super();
		this.configurationToPropertiesMap.put(new MyEnvironmentVariableConfig(THREAD_COUNT, "10"), THREAD_COUNT_PROPERTY_NAME);
	}
	
	@Override
	public Properties getProperties(String instanceName) {
		Properties properties = super.getProperties(instanceName);

		configurationToPropertiesMap.forEach((key, value) -> properties.put(value,
				getConfiguration(key.getEnvironmentVariableName(), key.getDefaultValue())));

		return properties;
	}
	
	private static String concatProps(String propPrefix, String propName) {
		return propPrefix + "." + propName;
	}
	
}