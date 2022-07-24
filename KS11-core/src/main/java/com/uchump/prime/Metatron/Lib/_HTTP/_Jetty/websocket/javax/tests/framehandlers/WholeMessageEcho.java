package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests.framehandlers;

import java.nio.ByteBuffer;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.MessageHandler;

public class WholeMessageEcho extends MessageHandler {
	@Override
	public void onBinary(ByteBuffer wholeMessage, Callback callback) {
		sendBinary(wholeMessage, callback, false);
	}

	@Override
	public void onText(String wholeMessage, Callback callback) {
		sendText(wholeMessage, callback, false);
	}
}