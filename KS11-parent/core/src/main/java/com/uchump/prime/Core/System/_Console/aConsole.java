package com.uchump.prime.Core.System._Console;

import static com.uchump.prime.Core.uAppUtils.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.uchump.prime.Core.Primitive.iFunctor;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iCycle;
import com.uchump.prime.Core.Primitive.Struct._Map;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Primitive.Struct.aSet;
import com.uchump.prime.Core.Primitive.Struct.aMap;
import com.uchump.prime.Core.System.Environment.aEnvironment;
import com.uchump.prime.Core.System.Event.iEventHandler;

public class aConsole extends aEnvironment<Integer, iEventHandler>
		implements iEventHandler, iCycle, Executor, com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.Executor<Exception> {

	public iEventHandler Target;

	public ConsoleLogger Logger;
	public ConsoleIO IO;
	protected Thread ConsoleInputThread;
	// protected ServerSocket Server;
	// http://localhost:8080/

	public boolean echo = false;
	public boolean running = false;

	public aList<iEventHandler> Subscribers = new aList<iEventHandler>();

	public iFunctor.Action Terminate = () -> this.terminate();
	public iFunctor.Action Log = () -> Log(this.toLog());
	public Consumer<String> Input = (a) -> this.post(a);

	protected aMap<String, aMap<String, iFunctor>> Options;
	protected aMap<String, iFunctor.Action> COMMANDS;

	public aConsole() {

	}

	public aConsole(iEventHandler target) {
		this();
		this.Target = target;
		this.IO = new ConsoleIO(target);
		this.setup();
		this.init();
	}

	public void setup() {
		this.Options = new aMap<String, aMap<String, iFunctor>>();
		this.COMMANDS = new aMap<String, iFunctor.Action>();
		this.Options.put("COMMAND", COMMANDS);

		// iFn.Action term = () -> this.terminate();
		this.COMMANDS.put("TERMINATE", Terminate);
		this.COMMANDS.put("LOG", Log);
		this.COMMANDS.put("INPUT", Input);

		this.data.put(":", "(COMMAND)");
		this.data.put("(COMMAND)", "[COMMANDS]");
		this.data.put("[COMMANDS]", COMMANDS);
		this.data.put("COMMAND.TERMINATE", Terminate);

	}

	public void init() {

		ConsoleInputThread = new Thread("ConsoleThread") {
			@Override
			public void run() {

				if (Target.isActive() && Target != null) {
					try {
						aConsole.this.next();
					} catch (Throwable t) {
						throw new RuntimeException(t);
					}
				}
			}
		};
		ConsoleInputThread.start();
		Log("Console Thread Start @" + this.ConsoleInputThread + "  ::  " + this.ConsoleInputThread.isAlive());
	}

	public void echo(boolean onOff) {
		this.echo = onOff;
	}

	// mainLoop lol
	@Override
	public void next() throws IOException {
		System.out.println("_CONSOLE LOOP START");
		String tmp = ":";
		System.out.flush();
		System.in.mark(0);
		System.in.reset();
		System.out.println(tmp);
		while (this.Target.isActive()) {// STEP INSTRUCTIONS

			synchronized (IO) {
				tmp = IO.readLine();

				// post(tmp);

				if (echo)
					System.out.println("$&: [" + tmp + "]");

			}
			boolean b = this.handle(tmp);
			System.in.mark(0);
			System.in.reset();

			// System.out.println("Console Loop Executed Successfully");

		}
		System.out.println("Shell Teminated");
	}

	// post input to ConsoleLogger's pending output
	public void post(String input) {
		if (echo) {
			Log(this.getClass().getSimpleName() + " recieved: " + input);
			if (Logger != null && Logger.active) {
				ConsoleLogger.toLog(input);
				ConsoleLogger.logOut();
			}
		}

		if (input.length() > 2)
			if (input.charAt(0) == ':')
				this.exe(input.substring(1));
	}

	public void post(Object o) {
		String r = "" + o.toString();
		post(r);
	}

	// take & handle console input, passes to subscribed listeners
	public boolean handle(String msg) {

		// Log(this.getClass().getSimpleName() + " recieved: " + msg);
		if (msg.equals("SHELL:TERMINATE")) {
			Log(this.toLog());
			post("SHELL:TERMINATE");
			if (Target != null)
				if (this.Target instanceof iCycle)
					((iCycle) this.Target).resume();
			this.terminate();
		}

		if (msg.equals(":LOG")) {
			post(":LOG");
			Log(this.toLog());

		}

		this.post(msg);

		for (iEventHandler c : this.getSubscribers()) {
			if (c.handle(msg))
				return true;
		}
		return false;
	}

	public void exe(String c) {
		iFunctor.Action f = this.COMMANDS.get(c);
		if (f != null)
			f.execute();
	}

	public void exe(iFunctor f) {
		if (f instanceof iFunctor.Action)
			((iFunctor.Action) f).execute();
	}

	// java concurrent
	@Override
	public void execute(Runnable command) {
		command.run();
	}

	// simple http
	@Override
	public <V> V submit(Callable<V> callable) throws Exception {
		return callable.call();
	}

	public iCollection<iEventHandler> getSubscribers() {
		if (this.Subscribers == null)
			this.Subscribers = new aList<iEventHandler>();
		return this.Subscribers;
	}

	public void flush() {
		System.out.flush();
		System.out.print("");
	}

	@Override
	public boolean isActive() {
		return this.Target.isActive();
	}

	@Override
	public void pause() {
		this.running = false;
		if (this.Target instanceof iCycle)
			((iCycle) this.Target).pause();

	}

	@Override
	public void resume() {
		if (this.Target instanceof iCycle)
			((iCycle) this.Target).resume();

	}

	@Override
	public void terminate() {
		this.pause();

		this.ConsoleInputThread.stop();

		if (this.Target instanceof iCycle)
			((iCycle) this.Target).terminate();

	}

	public String toLog() {

		String log = "";
		log += "[COMMANDS]\n";
		log += this.COMMANDS.toLog();

		log += this.Target;
		log += "\n";
		log += "#ThreadsActive- " + java.lang.Thread.activeCount();
		log += "\n";
		// log += ""+java.lang.Thread.getAllStackTraces();
		Map<Thread, StackTraceElement[]> threads = java.lang.Thread.getAllStackTraces();
		for (Map.Entry<Thread, StackTraceElement[]> t : threads.entrySet()) {

			if (!t.getKey().isDaemon()) {
				log += t.toString();
				log += "\n";
			}

		}
		log += "\n";

		return log;
	}

	public static class Command extends _Map.Entry<String, iFunctor.Action> implements iFunctor.Action {

		public Command(String name, iFunctor.Action action) {
			super(name, action);
		}

		@Override
		public void execute() {
			this.getValue().execute();
		}

		@Override
		public iFunctor.Action get() {
			this.execute();
			return this;
		}

	}

	public static class CommandMap extends aMap<String, iFunctor.Action> {
		@Override
		public String toLog() {
			String log = this.getClass().getSimpleName() + "{" + this.keys.size() + "}\n";
			log += this.keys.toSet().size() + "x" + this.values.size() + "\n";
			/*
			 * for (int i = 0; i < this.keys.size(); i++) { log += "[" + i + "]" +
			 * this.keys.get(i) + "|" + this.values.get(i) + "\n"; }
			 */
			aSet<String> L = new aSet<String>();
			aSet<String> thisKeys = this.keys.toSet();
			for (String k : thisKeys) {
				int i = keys.indexOf(k);
				// for(int i =0; i < this.keys.size(); i++) {
				// K k = keys.get(i);
				L.append("->[" + i + "] " + k + " |" + this.getAll(k));
			}
			for (int i = 0; i < L.size(); i++)
				log += "[" + i + "]" + L.get(i) + "\n";

			return log;
		}
	}

}
