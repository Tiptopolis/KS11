package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.Destroyable;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.LifeCycle;

/**
 * ShutdownThread is a shutdown hook thread implemented as singleton that
 * maintains a list of lifecycle instances that are registered with it and
 * provides ability to stop these lifecycles upon shutdown of the Java Virtual
 * Machine
 */
public class ShutdownThread extends Thread {
	private static final Logger LOG = LoggerFactory.getLogger(ShutdownThread.class);
	private static final ShutdownThread _thread = PrivilegedThreadFactory.newThread(() -> {
		return new ShutdownThread();
	});

	private final AutoLock _lock = new AutoLock();
	private boolean _hooked;
	private final List<LifeCycle> _lifeCycles = new CopyOnWriteArrayList<LifeCycle>();

	/**
	 * Default constructor for the singleton
	 *
	 * Registers the instance as shutdown hook with the Java Runtime
	 */
	private ShutdownThread() {
		super("JettyShutdownThread");
	}

	private void hook() {
		try (AutoLock l = _lock.lock()) {
			if (!_hooked)
				Runtime.getRuntime().addShutdownHook(this);
			_hooked = true;
		} catch (Exception e) {
			LOG.trace("IGNORED", e);
			LOG.info("shutdown already commenced");
		}
	}

	private void unhook() {
		try (AutoLock l = _lock.lock()) {
			_hooked = false;
			Runtime.getRuntime().removeShutdownHook(this);
		} catch (Exception e) {
			LOG.trace("IGNORED", e);
			LOG.debug("shutdown already commenced");
		}
	}

	/**
	 * Returns the instance of the singleton
	 *
	 * @return the singleton instance of the {@link ShutdownThread}
	 */
	public static ShutdownThread getInstance() {
		return _thread;
	}

	public static void register(LifeCycle... lifeCycles) {
		try (AutoLock l = _thread._lock.lock()) {
			_thread._lifeCycles.addAll(Arrays.asList(lifeCycles));
			if (_thread._lifeCycles.size() > 0)
				_thread.hook();
		}
	}

	public static void register(int index, LifeCycle... lifeCycles) {
		try (AutoLock l = _thread._lock.lock()) {
			_thread._lifeCycles.addAll(index, Arrays.asList(lifeCycles));
			if (_thread._lifeCycles.size() > 0)
				_thread.hook();
		}
	}

	public static void deregister(LifeCycle lifeCycle) {
		try (AutoLock l = _thread._lock.lock()) {
			_thread._lifeCycles.remove(lifeCycle);
			if (_thread._lifeCycles.size() == 0)
				_thread.unhook();
		}
	}

	public static boolean isRegistered(LifeCycle lifeCycle) {
		try (AutoLock l = _thread._lock.lock()) {
			return _thread._lifeCycles.contains(lifeCycle);
		}
	}

	@Override
	public void run() {
		for (LifeCycle lifeCycle : _thread._lifeCycles) {
			try {
				if (lifeCycle.isStarted()) {
					lifeCycle.stop();
					LOG.debug("Stopped {}", lifeCycle);
				}

				if (lifeCycle instanceof Destroyable) {
					((Destroyable) lifeCycle).destroy();
					LOG.debug("Destroyed {}", lifeCycle);
				}
			} catch (Exception ex) {
				LOG.debug("Unable to stop", ex);
			}
		}
	}
}