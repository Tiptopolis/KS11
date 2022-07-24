package com.uchump.prime.Metatron.Lib._HTTP;

import static com.uchump.prime.Core.uAppUtils.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javax.websocket.server.ServerEndpoint;

import com.sun.net.httpserver.HttpExchange;
import com.uchump.prime.Core.uAppUtils;
import com.uchump.prime.Core.Primitive.A_I.iCycle;
import com.uchump.prime.Core.System.Event.iHandler;
import com.uchump.prime.Metatron.MetatronSocketServer;

@ServerEndpoint(value = "/")
public class _SocketServer extends ServerSocket implements iHandler<HttpExchange>, iCycle {

	ServerSocket Socket;// technically works lol
	protected Thread ServerThread;
	protected boolean running = false;

	public _SocketServer() throws IOException {
		this(8080);
	}

	public _SocketServer(int port) throws IOException {
		this.Socket = new ServerSocket(port);
		System.out.println("Listening for connection on port " + port + " ....");
		/*
		 * while (true) { try (Socket socket = this.Socket.accept()) { Date today = new
		 * Date(); String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + today;
		 * socket.getOutputStream().write(httpResponse.getBytes("UTF-8")); } }
		 */
		this.running = true;

		ServerThread = new Thread("ServerThread") {
			@Override
			public void run() {

				if (_SocketServer.this.isActive()) {
					try {
						_SocketServer.this.next();
					} catch (Throwable t) {
						throw new RuntimeException(t);
					}
				}
			}
		};
		ServerThread.start();
		Log("Console Thread Start @" + this.ServerThread + "  ::  " + this.ServerThread.isAlive());
		System.out.println("Terminated connection on port 8080 ....");

	}

	@Override
	public void next() {
		while (this.isActive()) {
			synchronized (this) {
				try (Socket socket = this.Socket.accept()) {
					Date today = new Date();
					String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + today;
					socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
					Log("> "+httpResponse);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean handle(HttpExchange o) {
		Log(o);
		return false;
	}

	public void close() throws IOException {
		this.Socket.close();
		this.running = false;
	}

	public boolean isActive() {
		return !this.Socket.isClosed() && this.Socket.isBound();
	}

	@Override
	public String toString()
	{
		String s = uAppUtils.toIdString(this) +" : "+ this.Socket;
		return s;
	}
	
}
