package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client;

import java.util.List;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Response;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Result;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.util.BufferingResponseListener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpHeader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpHeaderValue;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpStatus;

/**
 * <p>
 * A protocol handler that handles the 100 response code.
 * </p>
 */
public class ContinueProtocolHandler implements ProtocolHandler {
	public static final String NAME = "continue";
	private static final String ATTRIBUTE = ContinueProtocolHandler.class.getName() + ".100continue";

	private final ResponseNotifier notifier;

	public ContinueProtocolHandler() {
		this.notifier = new ResponseNotifier();
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean accept(Request request, Response response) {
		boolean is100 = response.getStatus() == HttpStatus.CONTINUE_100;
		boolean expect100 = request.getHeaders().contains(HttpHeader.EXPECT, HttpHeaderValue.CONTINUE.asString());
		boolean handled100 = request.getAttributes().containsKey(ATTRIBUTE);
		return (is100 || expect100) && !handled100;
	}

	@Override
	public Response.Listener getResponseListener() {
		// Return new instances every time to keep track of the response content
		return new ContinueListener();
	}

	protected void onContinue(Request request) {
	}

	protected class ContinueListener extends BufferingResponseListener {
		@Override
		public void onSuccess(Response response) {
			// Handling of success must be done here and not from onComplete(),
			// since the onComplete() is not invoked because the request is not completed
			// yet.

			Request request = response.getRequest();
			HttpConversation conversation = ((HttpRequest) request).getConversation();
			// Mark the 100 Continue response as handled
			request.attribute(ATTRIBUTE, Boolean.TRUE);

			// Reset the conversation listeners, since we are going to receive another
			// response code
			conversation.updateResponseListeners(null);

			HttpExchange exchange = conversation.getExchanges().peekLast();
			if (response.getStatus() == HttpStatus.CONTINUE_100) {
				// All good, continue.
				exchange.resetResponse();
				exchange.proceed(null);
				onContinue(request);
			} else {
				// Server either does not support 100 Continue,
				// or it does and wants to refuse the request content,
				// or we got some other HTTP status code like a redirect.
				List<Response.ResponseListener> listeners = exchange.getResponseListeners();
				HttpContentResponse contentResponse = new HttpContentResponse(response, getContent(), getMediaType(),
						getEncoding());
				notifier.forwardSuccess(listeners, contentResponse);
				exchange.proceed(new HttpRequestException("Expectation failed", request));
			}
		}

		@Override
		public void onFailure(Response response, Throwable failure) {
			HttpConversation conversation = ((HttpRequest) response.getRequest()).getConversation();
			// Mark the 100 Continue response as handled
			conversation.setAttribute(ATTRIBUTE, Boolean.TRUE);
			// Reset the conversation listeners to allow the conversation to be completed
			conversation.updateResponseListeners(null);

			HttpExchange exchange = conversation.getExchanges().peekLast();
			assert exchange.getResponse() == response;
			List<Response.ResponseListener> listeners = exchange.getResponseListeners();
			HttpContentResponse contentResponse = new HttpContentResponse(response, getContent(), getMediaType(),
					getEncoding());
			notifier.forwardFailureComplete(listeners, exchange.getRequest(), exchange.getRequestFailure(),
					contentResponse, failure);
		}

		@Override
		public void onComplete(Result result) {
		}
	}
}