package com.uchump.prime.Metatron.Lib._HTTP._Spark.embeddedserver;

import java.util.HashMap;
import java.util.Map;

import com.uchump.prime.Metatron.Lib._HTTP._Spark.ExceptionMapper;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.embeddedserver.jetty.EmbeddedJettyFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.route.Routes;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.staticfiles.StaticFilesConfiguration;

/*
 * Holds and uses the factories for creating different Embedded servers.
 */
public class EmbeddedServers {

	// Default alternatives.
	public enum Identifiers {
		JETTY
	}

	private static Map<Object, EmbeddedServerFactory> factories = new HashMap<>();

	public static void initialize() {
		if (!factories.containsKey(Identifiers.JETTY)) {
			add(Identifiers.JETTY, new EmbeddedJettyFactory());
		}
	}

	public static Identifiers defaultIdentifier() {
		return Identifiers.JETTY;
	}

	@Deprecated
	public static EmbeddedServer create(Object identifier, Routes routeMatcher,
			StaticFilesConfiguration staticFilesConfiguration, boolean multipleHandlers) {

		return create(identifier, routeMatcher, ExceptionMapper.getServletInstance(), staticFilesConfiguration,
				multipleHandlers);
	}

	/**
	 * Creates an embedded server of type corresponding to the provided identifier.
	 *
	 * @param identifier               the identifier
	 * @param routeMatcher             the route matcher
	 * @param staticFilesConfiguration the static files configuration object
	 * @param multipleHandlers         true if other handlers exist
	 * @return the created EmbeddedServer object
	 */
	public static EmbeddedServer create(Object identifier, Routes routeMatcher, ExceptionMapper exceptionMapper,
			StaticFilesConfiguration staticFilesConfiguration, boolean multipleHandlers) {

		EmbeddedServerFactory factory = factories.get(identifier);

		if (factory != null) {
			return factory.create(routeMatcher, staticFilesConfiguration, exceptionMapper, multipleHandlers);
		} else {
			throw new RuntimeException("No embedded server matching the identifier");
		}
	}

	/**
	 * Adds an Embedded server factory for the provided identifier.
	 *
	 * @param identifier the identifier
	 * @param factory    the factory
	 */
	public static void add(Object identifier, EmbeddedServerFactory factory) {
		factories.put(identifier, factory);
	}

}