package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client;

import java.util.List;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Response;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Result;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpField;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpHeader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpStatus;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.EndPoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;

/**
 * <p>A protocol handler that handles HTTP 101 responses.</p>
 */
public class UpgradeProtocolHandler implements ProtocolHandler
{
    private final List<String> protocols = List.of("websocket", "h2c");

    @Override
    public String getName()
    {
        return "upgrade";
    }

    @Override
    public boolean accept(Request request, Response response)
    {
        boolean upgraded = HttpStatus.SWITCHING_PROTOCOLS_101 == response.getStatus();
        boolean accepted = false;
        if (upgraded)
            accepted = acceptHeaders(request, response);
        return upgraded && accepted;
    }

    protected boolean acceptHeaders(Request request, Response response)
    {
        HttpField responseUpgrade = response.getHeaders().getField(HttpHeader.UPGRADE);
        if (responseUpgrade != null && protocols.stream().anyMatch(responseUpgrade::contains))
            return true;
        // The response may not contain the Upgrade header, so check the request.
        HttpField requestUpgrade = request.getHeaders().getField(HttpHeader.UPGRADE);
        return requestUpgrade != null && protocols.stream().anyMatch(requestUpgrade::contains);
    }

    @Override
    public Response.Listener getResponseListener()
    {
        return new Response.Listener.Adapter()
        {
            @Override
            public void onComplete(Result result)
            {
                HttpResponse response = (HttpResponse)result.getResponse();
                HttpRequest request = (HttpRequest)response.getRequest();
                if (result.isSucceeded())
                {
                    try
                    {
                        HttpConversation conversation = request.getConversation();
                        HttpUpgrader upgrader = (HttpUpgrader)conversation.getAttribute(HttpUpgrader.class.getName());
                        if (upgrader == null)
                            throw new HttpResponseException("101 response without " + HttpUpgrader.class.getSimpleName(), response);
                        EndPoint endPoint = (EndPoint)conversation.getAttribute(EndPoint.class.getName());
                        if (endPoint == null)
                            throw new HttpResponseException("Upgrade without " + EndPoint.class.getSimpleName(), response);
                        upgrader.upgrade(response, endPoint, Callback.from(Callback.NOOP::succeeded, x -> forwardFailureComplete(request, null, response, x)));
                    }
                    catch (Throwable x)
                    {
                        forwardFailureComplete(request, null, response, x);
                    }
                }
                else
                {
                    forwardFailureComplete(request, result.getRequestFailure(), response, result.getResponseFailure());
                }
            }
        };
    }

    private void forwardFailureComplete(HttpRequest request, Throwable requestFailure, Response response, Throwable responseFailure)
    {
        HttpConversation conversation = request.getConversation();
        conversation.updateResponseListeners(null);
        List<Response.ResponseListener> responseListeners = conversation.getResponseListeners();
        ResponseNotifier notifier = new ResponseNotifier();
        notifier.forwardFailure(responseListeners, response, responseFailure);
        notifier.notifyComplete(responseListeners, new Result(request, requestFailure, response, responseFailure));
    }
}