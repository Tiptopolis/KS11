package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test.extensions;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Behavior;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Extension;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.ExtensionConfig;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.IncomingFrames;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OutgoingFrames;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.ExtensionStack;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.IdentityExtension;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test.IncomingFramesCapture;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test.OutgoingFramesCapture;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExtensionStackTest {
	private static final Logger LOG = LoggerFactory.getLogger(ExtensionStackTest.class);
	private static ExtensionStack stack;

	@BeforeAll
	public static void init() {
		stack = new ExtensionStack(new WebSocketComponents(), Behavior.SERVER);
	}

	@SuppressWarnings("unchecked")
	private <T> T assertIsExtension(String msg, Object obj, Class<T> clazz) {
		if (clazz.isAssignableFrom(obj.getClass())) {
			return (T) obj;
		}
		assertEquals("Expected " + msg + " class", clazz.getName(), obj.getClass().getName());
		return null;
	}

	@Test
	public void testStartIdentity() throws Exception {
		// 1 extension
		List<ExtensionConfig> configs = new ArrayList<>();
		configs.add(ExtensionConfig.parse("identity"));
		stack.negotiate(configs, configs);

		// Setup Listeners
		IncomingFrames session = new IncomingFramesCapture();
		OutgoingFrames connection = new OutgoingFramesCapture();
		stack.initialize(session, connection, null);

		// Dump
		LOG.debug("{}", stack.dump());

		// Should be no change to handlers
		Extension actualIncomingExtension = assertIsExtension("Incoming", stack.getNextIncoming(),
				IdentityExtension.class);
		Extension actualOutgoingExtension = assertIsExtension("Outgoing", stack.getNextOutgoing(),
				IdentityExtension.class);
		assertEquals(actualIncomingExtension, actualOutgoingExtension);
	}

	@Test
	public void testStartIdentityTwice() throws Exception {
		// 1 extension
		List<ExtensionConfig> configs = new ArrayList<>();
		configs.add(ExtensionConfig.parse("identity; id=A"));
		configs.add(ExtensionConfig.parse("identity; id=B"));
		stack.negotiate(configs, configs);

		// Setup Listeners
		IncomingFrames session = new IncomingFramesCapture();
		OutgoingFrames connection = new OutgoingFramesCapture();
		stack.initialize(session, connection, null);

		// Dump
		LOG.debug("{}", stack.dump());

		// Should be no change to handlers
		IdentityExtension actualIncomingExtension = assertIsExtension("Incoming", stack.getNextIncoming(),
				IdentityExtension.class);
		IdentityExtension actualOutgoingExtension = assertIsExtension("Outgoing", stack.getNextOutgoing(),
				IdentityExtension.class);

		assertThat("Incoming[identity].id", actualIncomingExtension.getParam("id"), is("A"));
		assertThat("Outgoing[identity].id", actualOutgoingExtension.getParam("id"), is("B"));
	}

	@Test
	public void testToString() {
		// Shouldn't cause a NPE.
		LOG.debug("Shouldn't cause a NPE: {}", stack.toString());
	}

	@Test
	public void testNegotiateChrome32() {
		String chromeRequest = "permessage-deflate; client_max_window_bits, x-webkit-deflate-frame";
		List<ExtensionConfig> requestedConfigs = ExtensionConfig.parseList(chromeRequest);
		stack.negotiate(requestedConfigs, requestedConfigs);

		List<ExtensionConfig> negotiated = stack.getNegotiatedExtensions();
		String response = ExtensionConfig.toHeaderValue(negotiated);

		assertThat("Negotiated Extensions", response, is("permessage-deflate"));
		LOG.debug("Shouldn't cause a NPE: {}", stack.toString());
	}
}