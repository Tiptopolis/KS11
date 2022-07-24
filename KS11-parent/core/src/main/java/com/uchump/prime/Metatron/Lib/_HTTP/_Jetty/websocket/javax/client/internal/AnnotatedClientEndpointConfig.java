package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.client.internal;

import java.util.Collections;
import java.util.List;
import javax.websocket.ClientEndpoint;
import javax.websocket.ClientEndpointConfig;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.exception.InvalidWebSocketException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.ClientEndpointConfigWrapper;

public class AnnotatedClientEndpointConfig extends ClientEndpointConfigWrapper {
	public AnnotatedClientEndpointConfig(ClientEndpoint anno, WebSocketComponents components) {
		Configurator configurator;
		try {
			configurator = components.getObjectFactory().createInstance(anno.configurator());
		} catch (Exception e) {
			StringBuilder err = new StringBuilder();
			err.append("Unable to instantiate ClientEndpoint.configurator() of ");
			err.append(anno.configurator().getName());
			err.append(" defined as annotation in ");
			err.append(anno.getClass().getName());
			throw new InvalidWebSocketException(err.toString(), e);
		}

		ClientEndpointConfig build = Builder.create().encoders(List.of(anno.encoders()))
				.decoders(List.of(anno.decoders())).preferredSubprotocols(List.of(anno.subprotocols()))
				.extensions(Collections.emptyList()).configurator(configurator).build();

		init(build);
	}
}