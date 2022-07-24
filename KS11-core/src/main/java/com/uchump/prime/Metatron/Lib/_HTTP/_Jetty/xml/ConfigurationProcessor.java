package com.uchump.prime.Metatron.Lib._HTTP._Jetty.xml;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.resource.Resource;

/**
 * A ConfigurationProcessor for non XmlConfiguration format files.
 * <p>
 * A file in non-XmlConfiguration file format may be processed by a {@link ConfigurationProcessor}
 * instance that is returned from a {@link ConfigurationProcessorFactory} instance discovered by the
 * {@code ServiceLoader} mechanism.  This is used to allow spring configuration files to be used instead of
 * {@code jetty.xml}
 */
public interface ConfigurationProcessor
{
    /**
     * Initialize a ConfigurationProcessor from provided Resource and XML
     *
     * @param resource the resource being read
     * @param root the parsed XML root node for the resource
     * @param configuration the configuration being used (typically for ref IDs)
     */
    void init(Resource resource, XmlParser.Node root, XmlConfiguration configuration);

    Object configure(Object obj) throws Exception;

    Object configure() throws Exception;
}