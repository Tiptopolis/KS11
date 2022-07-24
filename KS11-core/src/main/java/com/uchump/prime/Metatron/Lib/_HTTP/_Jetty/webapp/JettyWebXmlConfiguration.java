package com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.resource.Resource;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.xml.XmlConfiguration;

/**
 * JettyWebConfiguration.
 *
 * Looks for XmlConfiguration files in WEB-INF.  Searches in order for the first of jetty8-web.xml, jetty-web.xml or web-jetty.xml
 */
public class JettyWebXmlConfiguration extends AbstractConfiguration
{
    private static final Logger LOG = LoggerFactory.getLogger(JettyWebXmlConfiguration.class);

    public static final String PROPERTY_WEB_INF_URI = "web-inf.uri";
    public static final String PROPERTY_WEB_INF = "web-inf";
    public static final String XML_CONFIGURATION = "org.eclipse.jetty.webapp.JettyWebXmlConfiguration";
    public static final String JETTY_WEB_XML = "jetty-web.xml";

    public JettyWebXmlConfiguration()
    {
        addDependencies(WebXmlConfiguration.class, FragmentConfiguration.class, MetaInfConfiguration.class);
    }

    /**
     * Configure
     * Apply web-jetty.xml configuration
     *
     * @see Configuration#configure(WebAppContext)
     */
    @Override
    public void configure(WebAppContext context) throws Exception
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Configuring web-jetty.xml");

        Resource webInf = context.getWebInf();
        // handle any WEB-INF descriptors
        if (webInf != null && webInf.isDirectory())
        {
            // do jetty.xml file
            Resource jetty = webInf.addPath("jetty8-web.xml");
            if (!jetty.exists())
                jetty = webInf.addPath(JETTY_WEB_XML);
            if (!jetty.exists())
                jetty = webInf.addPath("web-jetty.xml");

            if (jetty.exists())
            {
                if (LOG.isDebugEnabled())
                    LOG.debug("Configure: {}", jetty);

                Object xmlAttr = context.getAttribute(XML_CONFIGURATION);
                context.removeAttribute(XML_CONFIGURATION);
                final XmlConfiguration jetty_config = xmlAttr instanceof XmlConfiguration ? (XmlConfiguration)xmlAttr : new XmlConfiguration(jetty);

                setupXmlConfiguration(context, jetty_config, webInf);

                try
                {
                    WebAppClassLoader.runWithServerClassAccess(() ->
                    {
                        jetty_config.configure(context);
                        return null;
                    });
                }
                catch (Exception e)
                {
                    LOG.warn("Error applying {}", jetty);
                    throw e;
                }
            }
        }
    }

    /**
     * Configures some well-known properties before the XmlConfiguration reads
     * the configuration.
     *
     * @param jettyConfig The configuration object.
     * @param webInf the WEB-INF location
     */
    private void setupXmlConfiguration(WebAppContext context, XmlConfiguration jettyConfig, Resource webInf) throws IOException
    {
        jettyConfig.setJettyStandardIdsAndProperties(context.getServer(), null);
        Map<String, String> props = jettyConfig.getProperties();
        props.put(PROPERTY_WEB_INF_URI, XmlConfiguration.normalizeURI(webInf.getURI().toString()));
        props.put(PROPERTY_WEB_INF, webInf.toString());
    }
}