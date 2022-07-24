package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.listeners;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.WebSocketConnectionListener;

public class AbstractListener implements WebSocketConnectionListener {
	protected Session _session;

	@Override
	public void onWebSocketConnect(Session session) {
		_session = session;
	}

	@Override
	public void onWebSocketError(Throwable thr) {
		thr.printStackTrace();
	}

	public void sendText(String message, boolean last) {
		try {
			_session.getRemote().sendPartialString(message, last);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void sendBinary(ByteBuffer message, boolean last) {
		try {
			_session.getRemote().sendPartialBytes(message, last);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}