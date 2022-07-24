package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.client.internal;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.HttpClient;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.resource.Resource;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.xml.XmlConfiguration;

public class XmlHttpClientProvider implements HttpClientProvider {
	private static final Logger LOG = LoggerFactory.getLogger(XmlHttpClientProvider.class);

	@Override
	public HttpClient newHttpClient() {
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		if (contextClassLoader == null)
			return null;

		URL resource = contextClassLoader.getResource("jetty-websocket-httpclient.xml");
		if (resource == null)
			return null;

		try {
			Thread.currentThread().setContextClassLoader(HttpClient.class.getClassLoader());
			return newHttpClient(resource);
		} finally {
			Thread.currentThread().setContextClassLoader(contextClassLoader);
		}
	}

	private static HttpClient newHttpClient(URL resource) {
		try {
			XmlConfiguration configuration = new XmlConfiguration(Resource.newResource(resource));
			return (HttpClient) configuration.configure();
		} catch (Throwable t) {
			LOG.warn("Failure to load HttpClient from XML {}", resource, t);
		}

		return null;
	}
}