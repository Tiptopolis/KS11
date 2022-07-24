package com.uchump.prime.Metatron.Lib._HTTP._Spark;
/**
 * A TemplateViewRoute is built up by a path.
 * TemplateViewRoute instead of returning the result of calling toString() as body, it returns the result of calling render method.
 * The primary purpose is provide a way to create generic and reusable components for rendering output using a Template Engine.
 * For example to render objects to html by using Freemarker template engine..
 *
 * @author alex
 */
@FunctionalInterface
public interface TemplateViewRoute {

    /**
     * Invoked when a request is made on this route's corresponding path e.g. '/hello'
     *
     * @param request  The request object providing information about the HTTP request
     * @param response The response object providing functionality for modifying the response
     * @return The content to be set in the response
     * @throws java.lang.Exception when handle fails
     */
    ModelAndView handle(Request request, Response response) throws Exception;

}