package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.resource;
import java.io.IOException;

/**
 * ResourceFactory.
 */
public interface ResourceFactory
{
    /**
     * Get a Resource from a provided String.
     * <p>
     * The behavior here is dependent on the
     * implementation of ResourceFactory.
     * The provided path can be resolved
     * against a known Resource, or can
     * be a from-scratch Resource.
     * </p>
     *
     * @param path The path to the resource
     * @return The resource, that might not actually exist (yet).
     * @throws IOException if unable to create Resource
     */
    Resource getResource(String path) throws IOException;
}