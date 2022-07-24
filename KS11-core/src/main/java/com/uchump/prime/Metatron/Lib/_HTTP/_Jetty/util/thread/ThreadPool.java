package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread;

import java.util.concurrent.Executor;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedAttribute;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedObject;

/**
 * <p>
 * A pool for threads.
 * </p>
 * <p>
 * A specialization of Executor interface that provides reporting methods (eg
 * {@link #getThreads()}) and the option of configuration methods (e.g. @link
 * {@link SizedThreadPool#setMaxThreads(int)}).
 * </p>
 */
@ManagedObject("Pool of Threads")
public interface ThreadPool extends Executor {
	/**
	 * Blocks until the thread pool is {@link LifeCycle#stop stopped}.
	 *
	 * @throws InterruptedException if thread was interrupted
	 */
	public void join() throws InterruptedException;

	/**
	 * @return The total number of threads currently in the pool
	 */
	@ManagedAttribute("number of threads in pool")
	public int getThreads();

	/**
	 * @return The number of idle threads in the pool
	 */
	@ManagedAttribute("number of idle threads in pool")
	public int getIdleThreads();

	/**
	 * @return True if the pool is low on threads
	 */
	@ManagedAttribute("indicates the pool is low on available threads")
	public boolean isLowOnThreads();

	/**
	 * <p>
	 * Specialized sub-interface of ThreadPool that allows to get/set the minimum
	 * and maximum number of threads of the pool.
	 * </p>
	 */
	public interface SizedThreadPool extends ThreadPool {
		/**
		 * @return the minimum number of threads
		 */
		int getMinThreads();

		/**
		 * @return the maximum number of threads
		 */
		int getMaxThreads();

		/**
		 * @param threads the minimum number of threads
		 */
		void setMinThreads(int threads);

		/**
		 * @param threads the maximum number of threads
		 */
		void setMaxThreads(int threads);

		/**
		 * @return a ThreadPoolBudget for this sized thread pool, or null of no
		 *         ThreadPoolBudget can be returned
		 */
		default ThreadPoolBudget getThreadPoolBudget() {
			return null;
		}
	}
}