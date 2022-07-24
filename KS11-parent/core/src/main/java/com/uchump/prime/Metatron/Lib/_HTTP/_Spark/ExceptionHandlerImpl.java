package com.uchump.prime.Metatron.Lib._HTTP._Spark;
public abstract class ExceptionHandlerImpl<T extends Exception> implements ExceptionHandler<T> {

    /**
     * Holds the type of exception that this filter will handle
     */
    protected Class<? extends T> exceptionClass;

    /**
     * Initializes the filter with the provided exception type
     *
     * @param exceptionClass Type of exception
     */
    public ExceptionHandlerImpl(Class<T> exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    /**
     * Returns type of exception that this filter will handle
     *
     * @return Type of exception
     */
    public Class<? extends T> exceptionClass() {
        return this.exceptionClass;
    }

    /**
     * Sets the type of exception that this filter will handle
     *
     * @param exceptionClass Type of exception
     */
    public void exceptionClass(Class<? extends T> exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    /**
     * Invoked when an exception that is mapped to this handler occurs during routing
     *
     * @param exception The exception that was thrown during routing
     * @param request   The request object providing information about the HTTP request
     * @param response  The response object providing functionality for modifying the response
     */
    public abstract void handle(T exception, Request request, Response response);
}