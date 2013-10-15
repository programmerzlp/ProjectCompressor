package org.zlp.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created with eclipse
 * 
 * @Description: 线程发放器
 * @author: programmer.zlp@qq.com
 * @Date: 2013-6-25
 * @Time: 上午11:54:42
 * 
 */
public enum ThreadGenerate {

	INSTANCE {

		@Override
		public ExecutorService createExecutorService() {
			return executorService;
		}

		@Override
		public void destroyExecutorService() {
			if (executorService == null) {
				return;
			}
			executorService.shutdown();
			try {
				executorService.awaitTermination(1, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		@Override
		public ExecutorService createTempExecutorService() {
			tempExecutorService = Executors.newCachedThreadPool();
			return tempExecutorService;
		}

		@Override
		public void destroyTempExecutorService() {
			if (tempExecutorService == null) {
				return;
			}
			tempExecutorService.shutdown();
			try {
				tempExecutorService.awaitTermination(1, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	};

	protected final ExecutorService executorService = Executors.newCachedThreadPool();// 单例线程池

	protected ExecutorService tempExecutorService = null;// 临时线程池

	public abstract ExecutorService createExecutorService();

	public abstract void destroyExecutorService();

	public abstract ExecutorService createTempExecutorService();

	public abstract void destroyTempExecutorService();

}