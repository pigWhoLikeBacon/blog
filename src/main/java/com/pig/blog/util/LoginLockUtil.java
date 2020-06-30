package com.pig.blog.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginLockUtil extends Thread {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static Thread t;
	private static String threadName;
	private static long begin = 0;
	private static long end = 0;
	
	private static final Integer MAX_LOGINS_COUNTER = 10;
	private static final Integer LOCK_TIME = 3600000;
	
    private static Integer loginsCounter = MAX_LOGINS_COUNTER;
	
	public LoginLockUtil(String name) {
		threadName = name;
	}
	
	public static void reSet() {
		loginsCounter = MAX_LOGINS_COUNTER;
	}
	
	public static boolean isUnlock() {
		return loginsCounter != 0;
	}
	
	public static Integer getTimes() {
		return loginsCounter;
	}
	
	public static void fail() {
		loginsCounter--; 
	}
	
	/**
	 * ms
	 * 
	 * @return
	 */
	public static Integer getTime() {
		if (begin == 0) {
			return LOCK_TIME;
		} else {
			end = System.currentTimeMillis();
			return (int) (LOCK_TIME - (end - begin));
		}
	}
	
	/**
	 * time: ms
	 * 
	 * @param time
	 * @return
	 */
	public static String timeToString(Integer time) {
		String h = ((time/1000)/60)/60 + "h";
		String m = ((time/1000)/60)%60 + "m";
		String s = (time/1000)%60 + "s";
		return h + m + s;
	}
	
	public static void lock() {
		LoginLockUtil lock = new LoginLockUtil("lock");
		lock.start();
	}
	
	/* thread */
	public void run() {
		begin = System.currentTimeMillis();
		logger.warn("Lock!Try more than " + MAX_LOGINS_COUNTER + " times.");
		try {
			Thread.sleep(LOCK_TIME);
			loginsCounter = MAX_LOGINS_COUNTER;
			begin = 0;
			t = null;
		} catch (InterruptedException e) {
			System.out.println("Thread " + threadName + " interrupted.");
			e.printStackTrace();
		}
	}

	public void start() {
		if (t == null) {
			t = new Thread(this, threadName);
			t.start();
		}
	}
	/* thread */
}
