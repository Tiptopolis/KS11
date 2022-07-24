package com.uchump.prime.Metatron;

import static com.uchump.prime.Core.uAppUtils.Log;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.uchump.prime.Core.Primitive.iFunctor;
import com.uchump.prime.Core.Primitive.A_I.iCycle;
import com.uchump.prime.Core.System.Event.iEventHandler;
import com.uchump.prime.Core.System.IO.GdxAdapter;
import com.uchump.prime.Core.System._Console.ConsoleLogger;
import com.uchump.prime.Core.System._Console.aConsole;

public class MetatronConsole extends aConsole implements GdxAdapter {

	public MetatronConsole() {
		super(Metatron.TheMetatron);
		this.echo = true;
	}

	@Override
	public void setup() {
		super.setup();

	}

	@Override
	public void terminate() {
		System.out.println("SHELL:TERMINATE...");
		this.running = false;
		if (this.Target instanceof iCycle)
			((iCycle) this.Target).terminate();
		Gdx.app.exit();
		System.exit(0);
	}

	public String toLog() {

		String log = "";

		log += "[SERVER]\n";
		log += Metatron.Server+"\n";
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

}
