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

package com.intland.codebeamer.pool;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.jdbc.pool.ConnectionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy(false)
@Service("poolMBeanRegister")
public class PoolBeanRegister {

	private static final Logger logger = LogManager.getLogger(PoolBeanRegister.class);

	@Autowired
	public PoolBeanRegister(DataSource dataSource) throws Throwable {		
		if (Proxy.isProxyClass(dataSource.getClass()) && getClass(dataSource).equals(org.apache.tomcat.jdbc.pool.DataSource.class)) {
			logger.info("Register {} into MBean", getClass(dataSource));
			MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
			mBeanServer.registerMBean(getConnectionPool(dataSource).getJmxPool(), new ObjectName("com.intland:type=DataSource,name=pool"));
		}
	}

	public ConnectionPool getConnectionPool(Object object) throws Throwable {
        if (Proxy.isProxyClass(object.getClass())) {
        	return call(object, org.apache.tomcat.jdbc.pool.DataSource.class.getMethod("getPool"));
        }
        
        throw new IllegalStateException("Object is not a proxy");
    }
	
	public Class getClass(Object object) throws Throwable {
        if (Proxy.isProxyClass(object.getClass())) {
        	return call(object, Object.class.getMethod("getClass"));
        }
        
        throw new IllegalStateException("Object is not a proxy");
    }
	
	private <T> T call(Object proxy, Method method) throws Throwable {
		return (T) Proxy.getInvocationHandler(proxy).invoke(proxy, method, null);
	}

}