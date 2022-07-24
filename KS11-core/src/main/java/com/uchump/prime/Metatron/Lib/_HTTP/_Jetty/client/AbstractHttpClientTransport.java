package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Connection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Promise;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedObject;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.ContainerLifeCycle;

@ManagedObject
public abstract class AbstractHttpClientTransport extends ContainerLifeCycle implements HttpClientTransport {
	private static final Logger LOG = LoggerFactory.getLogger(HttpClientTransport.class);

	private HttpClient client;
	private ConnectionPool.Factory factory;

	protected HttpClient getHttpClient() {
		return client;
	}

	@Override
	public void setHttpClient(HttpClient client) {
		this.client = client;
	}

	@Override
	public ConnectionPool.Factory getConnectionPoolFactory() {
		return factory;
	}

	@Override
	public void setConnectionPoolFactory(ConnectionPool.Factory factory) {
		this.factory = factory;
	}

	protected void connectFailed(Map<String, Object> context, Throwable failure) {
		if (LOG.isDebugEnabled())
			LOG.debug("Could not connect to {}", context.get(HTTP_DESTINATION_CONTEXT_KEY));
		@SuppressWarnings("unchecked")
		Promise<Connection> promise = (Promise<Connection>) context.get(HTTP_CONNECTION_PROMISE_CONTEXT_KEY);
		promise.failed(failure);
	}
}