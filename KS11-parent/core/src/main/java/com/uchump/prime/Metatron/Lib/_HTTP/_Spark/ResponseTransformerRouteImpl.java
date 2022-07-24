package com.uchump.prime.Metatron.Lib._HTTP._Spark;
/**
 * A ResponseTransformerRouteImpl is built up by a path (for url-matching) and the
 * implementation of the 'render' method. ResponseTransformerRoute instead of
 * returning the result of calling toString() as body, it returns the result of
 * calling render method.
 * The primary purpose of this kind of Route is provide a way to create generic
 * and reusable transformers. For example to convert an Object to JSON format.
 *
 * @author alex
 */
public abstract class ResponseTransformerRouteImpl extends RouteImpl {

    public static ResponseTransformerRouteImpl create(String path, Route route, ResponseTransformer transformer) {
        return create(path, Service.DEFAULT_ACCEPT_TYPE, route, transformer);
    }

    public static ResponseTransformerRouteImpl create(String path,
                                                      String acceptType,
                                                      Route route,
                                                      ResponseTransformer transformer) {
        return new ResponseTransformerRouteImpl(path, acceptType, route) {
            @Override
            public Object render(Object model) throws Exception {
                return transformer.render(model);
            }

            @Override
            public Object handle(Request request, Response response) throws Exception {
                return route.handle(request, response);
            }
        };
    }

    protected ResponseTransformerRouteImpl(String path, String acceptType, Route route) {
        super(path, acceptType, route);
    }

    /**
     * Method called for rendering the output.
     *
     * @param model object used to render output.
     * @return message that it is sent to client.
     */
    public abstract Object render(Object model) throws Exception;

}