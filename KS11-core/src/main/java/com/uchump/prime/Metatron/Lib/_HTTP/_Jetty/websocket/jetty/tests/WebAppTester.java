package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations.AnnotationConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.plus.webapp.EnvConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.plus.webapp.PlusConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ServerConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.ContextHandlerCollection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain.FS;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain.IO;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain.JAR;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain.MavenTestingUtils;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.TypeUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.ContainerLifeCycle;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.resource.PathResource;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.Configuration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.FragmentConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.JettyWebXmlConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.JmxConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.JndiConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.MetaInfConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebAppConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebAppContext;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebInfConfiguration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebXmlConfiguration;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.notNullValue;

/**
 * Utility to build out exploded directory WebApps.
 */
public class WebAppTester extends ContainerLifeCycle
{
    private static final Logger LOG = LoggerFactory.getLogger(WebAppTester.class);
    private final Path _testDir;
    private final Server _server;
    private final ServerConnector _serverConnector;
    private final ContextHandlerCollection _contexts;

    public WebAppTester()
    {
        this(null);
    }

    public WebAppTester(Path testDir)
    {
        if (testDir == null)
        {
            try
            {
                Path targetTestingPath = MavenTestingUtils.getTargetTestingPath();
                FS.ensureDirExists(targetTestingPath);
                _testDir = Files.createTempDirectory(targetTestingPath, "contexts");
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        else
        {
            _testDir = testDir;
            FS.ensureDirExists(_testDir);
        }

        _server = new Server();
        _serverConnector = new ServerConnector(_server);
        _server.addConnector(_serverConnector);
        _contexts = new ContextHandlerCollection();
        _server.setHandler(_contexts);
        addBean(_server);
    }

    @Override
    protected void doStop() throws Exception
    {
        // Recursively delete testDir when stopping.
    	com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.IO.delete(_testDir.toFile());
    }

    public Server getServer()
    {
        return _server;
    }

    public URI getServerUri()
    {
        if (!isStarted())
            throw new IllegalStateException("Not Started");
        return URI.create("http://localhost:" + getPort());
    }

    public int getPort()
    {
        return _serverConnector.getLocalPort();
    }

    public WebApp createWebApp(String contextPath)
    {
        return new WebApp(contextPath);
    }

    public class WebApp
    {
        private final WebAppContext _context;
        private final Path _contextDir;
        private final Path _webInf;
        private final Path _classesDir;
        private final Path _libDir;

        private WebApp(String contextPath)
        {
            // Ensure context directory.
            String contextDirName = contextPath.replace("/", "");
            if (contextDirName.length() == 0)
                contextDirName = "ROOT";
            _contextDir = _testDir.resolve(contextDirName);
            FS.ensureEmpty(_contextDir);

            // Ensure WEB-INF directories.
            _webInf = _contextDir.resolve("WEB-INF");
            FS.ensureDirExists(_webInf);
            _classesDir = _webInf.resolve("classes");
            FS.ensureDirExists(_classesDir);
            _libDir = _webInf.resolve("lib");
            FS.ensureDirExists(_libDir);

            // Configure the WebAppContext.
            _context = new WebAppContext();
            _context.setContextPath(contextPath);
            _context.setBaseResource(new PathResource(_contextDir));

            _context.setConfigurations(new Configuration[]
            {
                new JmxConfiguration(),
                new WebInfConfiguration(),
                new WebXmlConfiguration(),
                new MetaInfConfiguration(),
                new FragmentConfiguration(),
                new EnvConfiguration(),
                new PlusConfiguration(),
                new AnnotationConfiguration(),
                new JndiConfiguration(),
                new WebAppConfiguration(),
                new JettyWebXmlConfiguration()
            });
        }

        public WebAppContext getWebAppContext()
        {
            return _context;
        }

        public String getContextPath()
        {
            return _context.getContextPath();
        }

        public Path getContextDir()
        {
            return _contextDir;
        }

        public void addConfiguration(Configuration... configurations)
        {
            _context.addConfiguration(configurations);
        }

        public void createWebInf() throws IOException
        {
            String emptyWebXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<web-app\n" +
                "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "  xmlns=\"http://java.sun.com/xml/ns/javaee\"\n" +
                "  xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd\"\n" +
                "  metadata-complete=\"false\"\n" +
                "  version=\"3.0\">\n" +
                "</web-app>";

            File webXml = _webInf.resolve("web.xml").toFile();
            try (FileWriter out = new FileWriter(webXml))
            {
                out.write(emptyWebXml);
            }
        }

        public void copyWebInf(String testResourceName) throws IOException
        {
            File testWebXml = MavenTestingUtils.getTestResourceFile(testResourceName);
            Path webXml = _webInf.resolve("web.xml");
            IO.copy(testWebXml, webXml.toFile());
        }

        public void copyClass(Class<?> clazz) throws Exception
        {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            String endpointPath = TypeUtil.toClassReference(clazz);
            URL classUrl = cl.getResource(endpointPath);
            assertThat("Class URL for: " + clazz, classUrl, notNullValue());
            Path destFile = _classesDir.resolve(endpointPath);
            FS.ensureDirExists(destFile.getParent());
            File srcFile = new File(classUrl.toURI());
            IO.copy(srcFile, destFile.toFile());
        }

        public void copyLib(Class<?> clazz, String jarFileName) throws URISyntaxException, IOException
        {
            Path jarFile = _libDir.resolve(jarFileName);

            URL codeSourceURL = clazz.getProtectionDomain().getCodeSource().getLocation();
            assertThat("Class CodeSource URL is file scheme", codeSourceURL.getProtocol(), is("file"));

            File sourceCodeSourceFile = new File(codeSourceURL.toURI());
            if (sourceCodeSourceFile.isDirectory())
            {
                LOG.info("Creating " + jarFile + " from " + sourceCodeSourceFile);
                JAR.create(sourceCodeSourceFile, jarFile.toFile());
            }
            else
            {
                LOG.info("Copying " + sourceCodeSourceFile + " to " + jarFile);
                IO.copy(sourceCodeSourceFile, jarFile.toFile());
            }
        }

        public void deploy()
        {
            _contexts.addHandler(_context);
            _contexts.manage(_context);
            _context.setThrowUnavailableOnStartupException(true);
            if (LOG.isDebugEnabled())
                LOG.debug("{}", _context.dump());
        }
    }
}