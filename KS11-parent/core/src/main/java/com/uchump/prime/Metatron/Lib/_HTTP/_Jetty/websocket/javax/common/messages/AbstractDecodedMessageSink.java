package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.messages;

import java.lang.invoke.MethodHandle;
import java.util.List;
import java.util.stream.Collectors;
import javax.websocket.CloseReason;
import javax.websocket.Decoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.CoreSession;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.CloseException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.messages.MessageSink;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.decoders.RegisteredDecoder;

public abstract class AbstractDecodedMessageSink implements MessageSink {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractDecodedMessageSink.class);

	private final MethodHandle _methodHandle;
	private final MessageSink _messageSink;

	public AbstractDecodedMessageSink(CoreSession coreSession, MethodHandle methodHandle) {
		_methodHandle = methodHandle;

		try {
			_messageSink = newMessageSink(coreSession);
		} catch (Exception e) {
			// Throwing from here is an error implementation of the DecodedMessageSink.
			throw new RuntimeException(e);
		}
	}

	/**
	 * Invoke the MessageSink with the decoded message.
	 * 
	 * @param message the decoded message.
	 */
	void invoke(Object message) {
		try {
			_methodHandle.invoke(message);
		} catch (Throwable t) {
			throw new CloseException(CloseReason.CloseCodes.CANNOT_ACCEPT.getCode(), "Endpoint notification error", t);
		}
	}

	/**
	 * @return a message sink which will first decode the message then pass it to
	 *         {@link #_methodHandle}.
	 * @throws Exception for any error in creating the message sink.
	 */
	abstract MessageSink newMessageSink(CoreSession coreSession) throws Exception;

	@Override
	public void accept(Frame frame, Callback callback) {
		if (LOG.isDebugEnabled())
			LOG.debug("accepting frame {} for {}", frame, _messageSink);
		_messageSink.accept(frame, callback);
	}

	public abstract static class Basic<T extends Decoder> extends AbstractDecodedMessageSink {
		protected final List<T> _decoders;

		public Basic(CoreSession coreSession, MethodHandle methodHandle, List<RegisteredDecoder> decoders) {
			super(coreSession, methodHandle);
			if (decoders.isEmpty())
				throw new IllegalArgumentException("Require at least one decoder for " + this.getClass());
			_decoders = decoders.stream().map(RegisteredDecoder::<T>getInstance).collect(Collectors.toList());
		}
	}

	public abstract static class Stream<T extends Decoder> extends AbstractDecodedMessageSink {
		protected final T _decoder;

		public Stream(CoreSession coreSession, MethodHandle methodHandle, List<RegisteredDecoder> decoders) {
			super(coreSession, methodHandle);
			if (decoders.size() != 1)
				throw new IllegalArgumentException("Require exactly one decoder for " + this.getClass());
			_decoder = decoders.get(0).getInstance();
		}
	}
}