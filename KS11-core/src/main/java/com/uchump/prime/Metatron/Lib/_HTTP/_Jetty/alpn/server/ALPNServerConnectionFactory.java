package com.uchump.prime.Metatron.Lib._HTTP._Jetty.alpn.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;
import javax.net.ssl.SSLEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.AbstractConnection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.EndPoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ssl.ALPNProcessor.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Connector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.NegotiatingServerConnectionFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.TypeUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.Name;

public class ALPNServerConnectionFactory extends NegotiatingServerConnectionFactory {
	private static final Logger LOG = LoggerFactory.getLogger(ALPNServerConnectionFactory.class);

	private final List<Server> processors = new ArrayList<>();

	public ALPNServerConnectionFactory(@Name("protocols") String protocols) {
		this(protocols.trim().split(",", 0));
	}

	public ALPNServerConnectionFactory(@Name("protocols") String... protocols) {
		super("alpn", protocols);

		IllegalStateException failure = new IllegalStateException("No Server ALPNProcessors!");
		// Use a for loop on iterator so load exceptions can be caught and ignored
		TypeUtil.serviceProviderStream(ServiceLoader.load(Server.class)).forEach(provider -> {
			Server processor;
			try {
				processor = provider.get();
			} catch (Throwable x) {
				if (LOG.isDebugEnabled())
					LOG.debug(x.getMessage(), x);
				if (x != failure)
					failure.addSuppressed(x);
				return;
			}

			try {
				processor.init();
				processors.add(processor);
			} catch (Throwable x) {
				if (LOG.isDebugEnabled())
					LOG.debug("Could not initialize {}", processor, x);
				if (x != failure)
					failure.addSuppressed(x);
			}
		});

		if (LOG.isDebugEnabled()) {
			LOG.debug("protocols: {}", Arrays.asList(protocols));
			LOG.debug("processors: {}", processors);
		}

		if (processors.isEmpty())
			throw failure;
	}

	@Override
	protected AbstractConnection newServerConnection(Connector connector, EndPoint endPoint, SSLEngine engine,
			List<String> protocols, String defaultProtocol) {
		for (Server processor : processors) {
			if (processor.appliesTo(engine)) {
				if (LOG.isDebugEnabled())
					LOG.debug("{} for {} on {}", processor, engine, endPoint);
				ALPNServerConnection connection = new ALPNServerConnection(connector, endPoint, engine, protocols,
						defaultProtocol);
				processor.configure(engine, connection);
				return connection;
			}
		}

		if (LOG.isDebugEnabled())
			LOG.debug("No ALPNProcessor: {} {}", engine, endPoint);
		throw new IllegalStateException(
				"Connection rejected: No ALPN Processor for " + engine.getClass().getName() + " from " + processors);
	}
}