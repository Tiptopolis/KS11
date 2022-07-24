package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.TypeUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OutgoingFrames;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.Generator;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.lessThan;

/**
 * Capture outgoing network bytes.
 */
public class OutgoingNetworkBytesCapture implements OutgoingFrames {
	private final Generator generator;
	private final List<ByteBuffer> captured;

	public OutgoingNetworkBytesCapture(Generator generator) {
		this.generator = generator;
		this.captured = new ArrayList<>();
	}

	public void assertBytes(int idx, String expectedHex) {
		assertThat("Capture index does not exist", idx, lessThan(captured.size()));
		ByteBuffer buf = captured.get(idx);
		String actualHex = TypeUtil.toHexString(BufferUtil.toArray(buf)).toUpperCase(Locale.ENGLISH);
		assertThat("captured[" + idx + "]", actualHex, is(expectedHex.toUpperCase(Locale.ENGLISH)));
	}

	public List<ByteBuffer> getCaptured() {
		return captured;
	}

	@Override
	public void sendFrame(Frame frame, Callback callback, boolean batch) {
		ByteBuffer buf = BufferUtil.allocate(Generator.MAX_HEADER_LENGTH + frame.getPayloadLength());
		generator.generateWholeFrame(frame, buf);
		captured.add(buf);
		if (callback != null) {
			callback.succeeded();
		}
	}
}