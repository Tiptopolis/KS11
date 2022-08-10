package com.uchump.prime.Metatron;

import static com.uchump.prime.Core.uAppUtils.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.sun.net.httpserver.HttpServer;
import com.uchump.prime.Core.uAppUtils;
import com.uchump.prime.Core.Data.DataDirector;
import com.uchump.prime.Core.Primitive.Struct.aMultiMap;
import com.uchump.prime.Core.System.Environment._RuntimeEnv;
import com.uchump.prime.Core.System.Event.iEvent;
import com.uchump.prime.Core.System.Event.iEventHandler;
import com.uchump.prime.Core.System.IO.GdxAdapter;
import com.uchump.prime.Core.System._Console.aConsole;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.Executor;

public class Metatron extends _RuntimeEnv implements GdxAdapter{

	// handls time & comms between components
	// primary data repository

	public InputMultiplexer Multiplexer;
	//protected HttpServer Server;

	public static String localSystemPath = "C:/Users/Public/Data"; // Path, delimiter, caseMode
	public static String localInternalPath = "src/main/resources";
	public static String localDataPath = "/data";

	
	public static Metatron TheMetatron;
	public static MetatronSocketServer Server;
	public static MetatronData Data;
	public static MetatronConsole Console;

	private static MetatronBoot Boot;
	
	protected boolean running = false;

	public Metatron() {
		TheMetatron = this;
		Data = new MetatronData();
		this.Multiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(this.Multiplexer);
		this.running = true;
		try {
			Server = new MetatronSocketServer(8080);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Console = new MetatronConsole();
		/*try {
			this.Server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
			this.Server.setExecutor(Console);
			this.Server.start();
		} catch (IOException e) {

		}*/
		Log("METATRON CREATED");
		Log("[$} [TheMetatron]: " + uAppUtils.toToken(TheMetatron));
		Log("[$} [Data]: " + uAppUtils.toToken(Data));
		Log("[$} [Console]: " + Console);
		Log("[%} [LocalSystemPath]: " + localSystemPath);
		Log("[%} [LocalInternalPath]: " + localInternalPath);

	}
	
	public boolean isActive()
	{
		return this.running;
	}

	@Override
	public boolean handle(iEvent o) {
		Log("!@> " + o);
		return false;
	}
	
	@Override
	public boolean handle(String o) {
		Log("!@> " + o);
		return false;
	}
	
	@Override
	public void dispose() {
		/*if(this.Server!=null) {
		this.Server.stop(0);
		this.Server = null;
		}*/
		
		if(Server!=null) {
			try {
				Server.close();
				Server = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Console.terminate();
		// VTS.dispose();

	}
	
	/*public HttpServer getServer()
	{
		return this.Server;
	}*/
	
	public String toLog() {
		String log = "";
		log += this.toString() + "\n";

		return log;
	}
}
