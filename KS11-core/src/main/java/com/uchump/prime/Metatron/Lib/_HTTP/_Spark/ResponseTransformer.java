package com.uchump.prime.Metatron.Lib._HTTP._Spark;
@FunctionalInterface
public interface ResponseTransformer {

    /**
     * Method called for rendering the output.
     *
     * @param model object used to render output.
     * @return message that it is sent to client.
     * @throws java.lang.Exception when render fails
     */
    String render(Object model) throws Exception;

}