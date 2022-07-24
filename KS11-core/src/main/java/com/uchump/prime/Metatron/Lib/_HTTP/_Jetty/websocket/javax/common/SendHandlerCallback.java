package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common;

import javax.websocket.SendHandler;
import javax.websocket.SendResult;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;

/**
 * Wrapper of user provided {@link SendHandler} to Jetty internal
 * {@link Callback}
 */
public class SendHandlerCallback implements Callback {
	private final SendHandler sendHandler;

	public SendHandlerCallback(SendHandler sendHandler) {
		this.sendHandler = sendHandler;
	}

	@Override
	public void failed(Throwable x) {
		sendHandler.onResult(new SendResult(x));
	}

	@Override
	public void succeeded() {
		sendHandler.onResult(new SendResult());
	}
}