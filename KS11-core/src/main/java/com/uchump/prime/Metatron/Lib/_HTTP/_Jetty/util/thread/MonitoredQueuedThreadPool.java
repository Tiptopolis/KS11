package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread;

import java.util.concurrent.BlockingQueue;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BlockingArrayQueue;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedAttribute;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedObject;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedOperation;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.statistic.CounterStatistic;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.statistic.SampleStatistic;

/**
 * <p>
 * A {@link QueuedThreadPool} subclass that monitors its own activity by
 * recording queue and task statistics.
 * </p>
 */
@ManagedObject
public class MonitoredQueuedThreadPool extends QueuedThreadPool {
	private final CounterStatistic queueStats = new CounterStatistic();
	private final SampleStatistic queueLatencyStats = new SampleStatistic();
	private final SampleStatistic taskLatencyStats = new SampleStatistic();
	private final CounterStatistic threadStats = new CounterStatistic();

	public MonitoredQueuedThreadPool() {
		this(256);
	}

	public MonitoredQueuedThreadPool(int maxThreads) {
		this(maxThreads, maxThreads, 24 * 3600 * 1000, new BlockingArrayQueue<>(maxThreads, 256));
	}

	public MonitoredQueuedThreadPool(int maxThreads, int minThreads, int idleTimeOut, BlockingQueue<Runnable> queue) {
		super(maxThreads, minThreads, idleTimeOut, queue);
		addBean(queueStats);
		addBean(queueLatencyStats);
		addBean(taskLatencyStats);
		addBean(threadStats);
	}

	@Override
	public void execute(final Runnable job) {
		queueStats.increment();
		long begin = System.nanoTime();
		super.execute(new Runnable() {
			@Override
			public void run() {
				long queueLatency = System.nanoTime() - begin;
				queueStats.decrement();
				threadStats.increment();
				queueLatencyStats.record(queueLatency);
				long start = System.nanoTime();
				try {
					job.run();
				} finally {
					long taskLatency = System.nanoTime() - start;
					threadStats.decrement();
					taskLatencyStats.record(taskLatency);
				}
			}

			@Override
			public String toString() {
				return job.toString();
			}
		});
	}

	/**
	 * Resets the statistics.
	 */
	@ManagedOperation(value = "resets the statistics", impact = "ACTION")
	public void reset() {
		queueStats.reset();
		queueLatencyStats.reset();
		taskLatencyStats.reset();
		threadStats.reset(0);
	}

	/**
	 * @return the number of tasks executed
	 */
	@ManagedAttribute("the number of tasks executed")
	public long getTasks() {
		return taskLatencyStats.getCount();
	}

	/**
	 * @return the maximum number of busy threads
	 */
	@ManagedAttribute("the maximum number of busy threads")
	public int getMaxBusyThreads() {
		return (int) threadStats.getMax();
	}

	/**
	 * @return the maximum task queue size
	 */
	@ManagedAttribute("the maximum task queue size")
	public int getMaxQueueSize() {
		return (int) queueStats.getMax();
	}

	/**
	 * @return the average time a task remains in the queue, in nanoseconds
	 */
	@ManagedAttribute("the average time a task remains in the queue, in nanoseconds")
	public long getAverageQueueLatency() {
		return (long) queueLatencyStats.getMean();
	}

	/**
	 * @return the maximum time a task remains in the queue, in nanoseconds
	 */
	@ManagedAttribute("the maximum time a task remains in the queue, in nanoseconds")
	public long getMaxQueueLatency() {
		return queueLatencyStats.getMax();
	}

	/**
	 * @return the average task execution time, in nanoseconds
	 */
	@ManagedAttribute("the average task execution time, in nanoseconds")
	public long getAverageTaskLatency() {
		return (long) taskLatencyStats.getMean();
	}

	/**
	 * @return the maximum task execution time, in nanoseconds
	 */
	@ManagedAttribute("the maximum task execution time, in nanoseconds")
	public long getMaxTaskLatency() {
		return taskLatencyStats.getMax();
	}
}