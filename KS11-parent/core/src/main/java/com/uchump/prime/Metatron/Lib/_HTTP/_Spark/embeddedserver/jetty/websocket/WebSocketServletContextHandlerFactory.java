package com.uchump.prime.Metatron.Lib._HTTP._Spark.embeddedserver.jetty.websocket;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.pathmap.ServletPathSpec;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletContextHandler;

/**
 * Creates websocket servlet context handlers.
 */
public class WebSocketServletContextHandlerFactory {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketServletContextHandlerFactory.class);

	/**
	 * Creates a new websocket servlet context handler.
	 *
	 * @param webSocketHandlers          webSocketHandlers
	 * @param webSocketIdleTimeoutMillis webSocketIdleTimeoutMillis
	 * @return a new websocket servlet context handler or 'null' if creation failed.
	 */
	public static ServletContextHandler create(Map<String, WebSocketHandlerWrapper> webSocketHandlers,
			Optional<Long> webSocketIdleTimeoutMillis) {
		ServletContextHandler webSocketServletContextHandler = null;
		if (webSocketHandlers != null) {
			try {
				webSocketServletContextHandler = new ServletContextHandler(null, "/", true, false);
				WebSocketUpgradeFilter webSocketUpgradeFilter = WebSocketUpgradeFilter
						.configureContext(webSocketServletContextHandler);
				if (webSocketIdleTimeoutMillis.isPresent()) {
					webSocketUpgradeFilter.getFactory().getPolicy().setIdleTimeout(webSocketIdleTimeoutMillis.get());
				}
				// Since we are configuring WebSockets before the ServletContextHandler and
				// WebSocketUpgradeFilter is
				// even initialized / started, then we have to pre-populate the configuration
				// that will eventually
				// be used by Jetty's WebSocketUpgradeFilter.
				NativeWebSocketConfiguration webSocketConfiguration = (NativeWebSocketConfiguration) webSocketServletContextHandler
						.getServletContext().getAttribute(NativeWebSocketConfiguration.class.getName());
				for (String path : webSocketHandlers.keySet()) {
					WebSocketCreator webSocketCreator = WebSocketCreatorFactory.create(webSocketHandlers.get(path));
					webSocketConfiguration.addMapping(new ServletPathSpec(path), webSocketCreator);
				}
			} catch (Exception ex) {
				logger.error("creation of websocket context handler failed.", ex);
				webSocketServletContextHandler = null;
			}
		}
		return webSocketServletContextHandler;
	}

}