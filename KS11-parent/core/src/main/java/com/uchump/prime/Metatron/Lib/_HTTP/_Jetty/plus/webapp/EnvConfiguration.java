package com.uchump.prime.Metatron.Lib._HTTP._Jetty.plus.webapp;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.jndi.ContextFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.jndi.NamingContext;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.jndi.NamingUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.jndi.local.localContextRoot;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.plus.jndi.EnvEntry;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.plus.jndi.NamingDump;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.plus.jndi.NamingEntryUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.resource.Resource;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.AbstractConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.FragmentConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.JettyWebXmlConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.MetaInfConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebAppClassLoader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebAppContext;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebXmlConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.xml.XmlConfiguration;

/**
 * EnvConfiguration
 */
public class EnvConfiguration extends AbstractConfiguration
{
    private static final Logger LOG = LoggerFactory.getLogger(EnvConfiguration.class);

    private static final String JETTY_ENV_BINDINGS = "org.eclipse.jetty.jndi.EnvConfiguration";
    private Resource jettyEnvXmlResource;
    private NamingDump _dumper;

    public EnvConfiguration()
    {
        addDependencies(WebXmlConfiguration.class, MetaInfConfiguration.class, FragmentConfiguration.class);
        addDependents(PlusConfiguration.class, JettyWebXmlConfiguration.class);
        protectAndExpose("org.eclipse.jetty.jndi.");
    }

    public void setJettyEnvResource(Resource resource)
    {
        this.jettyEnvXmlResource = resource;
    }

    public void setJettyEnvXml(URL url)
    {
        this.jettyEnvXmlResource = Resource.newResource(url);
    }

    @Override
    public void preConfigure(WebAppContext context) throws Exception
    {
        //create a java:comp/env
        createEnvContext(context);
    }

    @Override
    public void configure(WebAppContext context) throws Exception
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Created java:comp/env for webapp {}", context.getContextPath());

        //check to see if an explicit file has been set, if not,
        //look in WEB-INF/jetty-env.xml
        if (jettyEnvXmlResource == null)
        {
            //look for a file called WEB-INF/jetty-env.xml
            //and process it if it exists
        	com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.resource.Resource webInf = context.getWebInf();
            if (webInf != null && webInf.isDirectory())
            {
            	com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.resource.Resource jettyEnv = webInf.addPath("jetty-env.xml");
                if (jettyEnv.exists())
                {
                    jettyEnvXmlResource = jettyEnv;
                }
            }
        }

        if (jettyEnvXmlResource != null)
        {
            synchronized (localContextRoot.getRoot())
            {
                // create list and listener to remember the bindings we make.
                final List<Bound> bindings = new ArrayList<Bound>();
                NamingContext.Listener listener = new NamingContext.Listener()
                {
                    @Override
                    public void unbind(NamingContext ctx, Binding binding)
                    {
                    }

                    @Override
                    public Binding bind(NamingContext ctx, Binding binding)
                    {
                        bindings.add(new Bound(ctx, binding.getName()));
                        return binding;
                    }
                };

                try
                {
                    localContextRoot.getRoot().addListener(listener);
                    XmlConfiguration configuration = new XmlConfiguration(jettyEnvXmlResource);
                    configuration.setJettyStandardIdsAndProperties(context.getServer(), null);
                    WebAppClassLoader.runWithServerClassAccess(() ->
                    {
                        configuration.configure(context);
                        return null;
                    });
                }
                finally
                {
                    localContextRoot.getRoot().removeListener(listener);
                    context.setAttribute(JETTY_ENV_BINDINGS, bindings);
                }
            }
        }

        //add java:comp/env entries for any EnvEntries that have been defined so far
        bindEnvEntries(context);

        _dumper = new NamingDump(context.getClassLoader(), "java:comp");
        context.addBean(_dumper);
    }

    /**
     * Remove jndi setup from start
     *
     * @throws Exception if unable to deconfigure
     */
    @Override
    public void deconfigure(WebAppContext context) throws Exception
    {
        context.removeBean(_dumper);
        _dumper = null;

        //get rid of any bindings for comp/env for webapp
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(context.getClassLoader());
        ContextFactory.associateClassLoader(context.getClassLoader());
        try
        {
            Context ic = new InitialContext();
            Context compCtx = (Context)ic.lookup("java:comp");
            compCtx.destroySubcontext("env");

            //unbind any NamingEntries that were configured in this webapp's name space
            @SuppressWarnings("unchecked")
            List<Bound> bindings = (List<Bound>)context.getAttribute(JETTY_ENV_BINDINGS);
            context.setAttribute(JETTY_ENV_BINDINGS, null);
            if (bindings != null)
            {
                Collections.reverse(bindings);
                for (Bound b : bindings)
                {
                    b._context.destroySubcontext(b._name);
                }
            }
        }
        catch (NameNotFoundException e)
        {
            LOG.warn("Unable to destroy InitialContext", e);
        }
        finally
        {
            ContextFactory.disassociateClassLoader();
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    /**
     * Remove all jndi setup
     *
     * @throws Exception if unable to destroy
     */
    @Override
    public void destroy(WebAppContext context) throws Exception
    {
        try
        {
            //unbind any NamingEntries that were configured in this webapp's name space           
            NamingContext scopeContext = (NamingContext)NamingEntryUtil.getContextForScope(context);
            scopeContext.getParent().destroySubcontext(scopeContext.getName());
        }
        catch (NameNotFoundException e)
        {
            LOG.trace("IGNORED", e);
            LOG.debug("No jndi entries scoped to webapp {}", context);
        }
        catch (NamingException e)
        {
            LOG.debug("Error unbinding jndi entries scoped to webapp {}", context, e);
        }
    }

    /**
     * Bind all EnvEntries that have been declared, so that the processing of the
     * web.xml file can potentially override them.
     *
     * We first bind EnvEntries declared in Server scope, then WebAppContext scope.
     *
     * @param context the context to use for the object scope
     * @throws NamingException if unable to bind env entries
     */
    public void bindEnvEntries(WebAppContext context)
        throws NamingException
    {
        InitialContext ic = new InitialContext();
        Context envCtx = (Context)ic.lookup("java:comp/env");

        LOG.debug("Binding env entries from the jvm scope");
        doBindings(envCtx, null);

        LOG.debug("Binding env entries from the server scope");
        doBindings(envCtx, context.getServer());

        LOG.debug("Binding env entries from the context scope");
        doBindings(envCtx, context);
    }

    private void doBindings(Context envCtx, Object scope) throws NamingException
    {
        for (EnvEntry ee : NamingEntryUtil.lookupNamingEntries(scope, EnvEntry.class))
        {
            ee.bindToENC(ee.getJndiName());
            Name namingEntryName = NamingEntryUtil.makeNamingEntryName(null, ee);
            NamingUtil.bind(envCtx, namingEntryName.toString(), ee); //also save the EnvEntry in the context so we can check it later
        }
    }

    protected void createEnvContext(WebAppContext wac)
        throws NamingException
    {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(wac.getClassLoader());
        ContextFactory.associateClassLoader(wac.getClassLoader());
        try
        {
            Context context = new InitialContext();
            Context compCtx = (Context)context.lookup("java:comp");
            compCtx.createSubcontext("env");
        }
        finally
        {
            ContextFactory.disassociateClassLoader();
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    private static class Bound
    {
        final NamingContext _context;
        final String _name;

        Bound(NamingContext context, String name)
        {
            _context = context;
            _name = name;
        }
    }
}