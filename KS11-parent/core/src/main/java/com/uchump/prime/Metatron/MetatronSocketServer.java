package com.uchump.prime.Metatron;

import static com.uchump.prime.Core.uAppUtils.Log;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.websocket.server.ServerEndpoint;

import com.uchump.prime.Core.Primitive.A_I.iCycle;
import com.uchump.prime.Core.System.Event.iHandler;
import com.uchump.prime.Core.System._Console.aConsole;
import com.uchump.prime.Metatron.Lib._HTTP._SocketServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.io.OutputStream;
import java.net.InetSocketAddress;

@ServerEndpoint(value = "/ping")
public class MetatronSocketServer extends _SocketServer {
	// public ThreadPoolExecutor ThreadExecutor =
	// (ThreadPoolExecutor)Executors.newCachedThreadPool();
	// http://localhost:8080/

	protected Thread ServerThread;

	protected boolean running = false;

	public MetatronSocketServer(int port) throws IOException {
		super(port);
		
	}

	

}
