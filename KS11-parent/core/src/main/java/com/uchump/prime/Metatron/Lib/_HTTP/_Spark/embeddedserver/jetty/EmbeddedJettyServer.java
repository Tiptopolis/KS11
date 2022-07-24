package com.uchump.prime.Metatron.Lib._HTTP._Spark.embeddedserver.jetty;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Connector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Handler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.HandlerList;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.thread.ThreadPool;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.embeddedserver.EmbeddedServer;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.embeddedserver.jetty.websocket.WebSocketHandlerWrapper;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.embeddedserver.jetty.websocket.WebSocketServletContextHandlerFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.ssl.SslStores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Spark server implementation
 *
 * @author Per Wendel
 */
public class EmbeddedJettyServer implements EmbeddedServer {

	private static final int SPARK_DEFAULT_PORT = 4567;
	private static final String NAME = "Spark";

	private final JettyServerFactory serverFactory;
	private final Handler handler;
	private Server server;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private Map<String, WebSocketHandlerWrapper> webSocketHandlers;
	private Optional<Long> webSocketIdleTimeoutMillis;

	private ThreadPool threadPool = null;
	private boolean trustForwardHeaders = true; // true by default

	public EmbeddedJettyServer(JettyServerFactory serverFactory, Handler handler) {
		this.serverFactory = serverFactory;
		this.handler = handler;
	}

	@Override
	public void configureWebSockets(Map<String, WebSocketHandlerWrapper> webSocketHandlers,
			Optional<Long> webSocketIdleTimeoutMillis) {

		this.webSocketHandlers = webSocketHandlers;
		this.webSocketIdleTimeoutMillis = webSocketIdleTimeoutMillis;
	}

	@Override
	public void trustForwardHeaders(boolean trust) {
		this.trustForwardHeaders = trust;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int ignite(String host, int port, SslStores sslStores, int maxThreads, int minThreads,
			int threadIdleTimeoutMillis) throws Exception {

		boolean hasCustomizedConnectors = false;

		if (port == 0) {
			try (ServerSocket s = new ServerSocket(0)) {
				port = s.getLocalPort();
			} catch (IOException e) {
				logger.error("Could not get first available port (port set to 0), using default: {}",
						SPARK_DEFAULT_PORT);
				port = SPARK_DEFAULT_PORT;
			}
		}

		// Create instance of jetty server with either default or supplied queued thread
		// pool
		if (threadPool == null) {
			server = serverFactory.create(maxThreads, minThreads, threadIdleTimeoutMillis);
		} else {
			server = serverFactory.create(threadPool);
		}

		ServerConnector connector;

		if (sslStores == null) {
			connector = SocketConnectorFactory.createSocketConnector(server, host, port, trustForwardHeaders);
		} else {
			connector = SocketConnectorFactory.createSecureSocketConnector(server, host, port, sslStores,
					trustForwardHeaders);
		}

		Connector previousConnectors[] = server.getConnectors();
		server = connector.getServer();
		if (previousConnectors.length != 0) {
			server.setConnectors(previousConnectors);
			hasCustomizedConnectors = true;
		} else {
			server.setConnectors(new Connector[] { connector });
		}

		ServletContextHandler webSocketServletContextHandler = WebSocketServletContextHandlerFactory
				.create(webSocketHandlers, webSocketIdleTimeoutMillis);

		// Handle web socket routes
		if (webSocketServletContextHandler == null) {
			server.setHandler(handler);
		} else {
			List<Handler> handlersInList = new ArrayList<>();
			handlersInList.add(handler);

			// WebSocket handler must be the last one
			if (webSocketServletContextHandler != null) {
				handlersInList.add(webSocketServletContextHandler);
			}

			HandlerList handlers = new HandlerList();
			handlers.setHandlers(handlersInList.toArray(new Handler[handlersInList.size()]));
			server.setHandler(handlers);
		}

		logger.info("== {} has ignited ...", NAME);
		if (hasCustomizedConnectors) {
			logger.info(">> Listening on Custom Server ports!");
		} else {
			logger.info(">> Listening on {}:{}", host, port);
		}

		server.start();
		return port;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void join() throws InterruptedException {
		server.join();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void extinguish() {
		logger.info(">>> {} shutting down ...", NAME);
		try {
			if (server != null) {
				server.stop();
			}
		} catch (Exception e) {
			logger.error("stop failed", e);
			System.exit(100); // NOSONAR
		}
		logger.info("done");
	}

	@Override
	public int activeThreadCount() {
		if (server == null) {
			return 0;
		}
		return server.getThreadPool().getThreads() - server.getThreadPool().getIdleThreads();
	}

	/**
	 * Sets optional thread pool for jetty server. This is useful for overriding the
	 * default thread pool behaviour for example
	 * io.dropwizard.metrics.jetty9.InstrumentedQueuedThreadPool.
	 * 
	 * @param threadPool thread pool
	 * @return Builder pattern - returns this instance
	 */
	public EmbeddedJettyServer withThreadPool(ThreadPool threadPool) {
		this.threadPool = threadPool;
		return this;
	}
}