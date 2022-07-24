package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Handler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Server;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.ContextHandlerCollection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain.FS;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain.IO;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain.JAR;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain.MavenTestingUtils;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.TypeUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.resource.PathResource;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebAppContext;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.server.config.JavaxWebSocketConfiguration;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.notNullValue;


/**
 * Utility to build out exploded directory WebApps, in the /target/tests/ directory, for testing out servers that use javax.websocket endpoints.
 * <p>
 * This is particularly useful when the WebSocket endpoints are discovered via the javax.websocket annotation scanning.
 */
public class WSServer extends LocalServer implements LocalFuzzer.Provider
{
    private static final Logger LOG = LoggerFactory.getLogger(WSServer.class);
    private final Path testDir;
    private final ContextHandlerCollection contexts = new ContextHandlerCollection();

    public WSServer()
    {
        String baseDirName = Long.toString(Math.abs(new Random().nextLong()));
        this.testDir = MavenTestingUtils.getTargetTestingPath(baseDirName);
        if (Files.exists(testDir))
            throw new IllegalStateException("TestDir already exists.");
        FS.ensureDirExists(testDir);
    }

    public WSServer(Path testDir)
    {
        this.testDir = testDir;
    }

    public WebApp createWebApp(String contextName)
    {
        return new WebApp(contextName);
    }

    @Override
    protected Handler createRootHandler(Server server)
    {
        return contexts;
    }

    public class WebApp
    {
        private final WebAppContext context;
        private final Path contextDir;
        private final Path webInf;
        private final Path classesDir;
        private final Path libDir;

        private WebApp(String contextName)
        {
            // Ensure context directory.
            contextDir = testDir.resolve(contextName);
            FS.ensureEmpty(contextDir);

            // Ensure WEB-INF directories.
            webInf = contextDir.resolve("WEB-INF");
            FS.ensureDirExists(webInf);
            classesDir = webInf.resolve("classes");
            FS.ensureDirExists(classesDir);
            libDir = webInf.resolve("lib");
            FS.ensureDirExists(libDir);

            // Configure the WebAppContext.
            context = new WebAppContext();
            context.setContextPath("/" + contextName);
            context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
            context.setBaseResource(new PathResource(contextDir));
            context.setAttribute("org.eclipse.jetty.websocket.javax", Boolean.TRUE);
            context.addConfiguration(new JavaxWebSocketConfiguration());
        }

        public WebAppContext getWebAppContext()
        {
            return context;
        }

        public String getContextPath()
        {
            return context.getContextPath();
        }

        public Path getContextDir()
        {
            return contextDir;
        }

        public void createWebInf() throws IOException
        {
            copyWebInf("empty-web.xml");
        }

        public void copyWebInf(String testResourceName) throws IOException
        {
            File testWebXml = MavenTestingUtils.getTestResourceFile(testResourceName);
            Path webXml = webInf.resolve("web.xml");
            IO.copy(testWebXml, webXml.toFile());
        }

        public void copyClass(Class<?> clazz) throws Exception
        {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            String endpointPath = TypeUtil.toClassReference(clazz);
            URL classUrl = cl.getResource(endpointPath);
            assertThat("Class URL for: " + clazz, classUrl, notNullValue());
            Path destFile = classesDir.resolve(endpointPath);
            FS.ensureDirExists(destFile.getParent());
            File srcFile = new File(classUrl.toURI());
            IO.copy(srcFile, destFile.toFile());
        }

        public void copyLib(Class<?> clazz, String jarFileName) throws URISyntaxException, IOException
        {
            Path jarFile = libDir.resolve(jarFileName);

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
            contexts.addHandler(context);
            contexts.manage(context);
            context.setThrowUnavailableOnStartupException(true);
            if (LOG.isDebugEnabled())
                LOG.debug("{}", context.dump());
        }
    }
}