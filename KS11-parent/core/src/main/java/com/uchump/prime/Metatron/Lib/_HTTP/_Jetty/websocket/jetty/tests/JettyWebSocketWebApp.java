package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain.FS;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain.MavenTestingUtils;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.IO;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.TypeUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.resource.PathResource;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp.WebAppContext;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.config.JettyWebSocketConfiguration;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.notNullValue;

public class JettyWebSocketWebApp extends WebAppContext
{
    private static final Logger LOG = LoggerFactory.getLogger(JettyWebSocketWebApp.class);

    private final Path contextDir;
    private final Path webInf;
    private final Path classesDir;

    public JettyWebSocketWebApp(String contextName)
    {
        // Ensure context directory.
        Path testDir = MavenTestingUtils.getTargetTestingPath(JettyWebSocketWebApp.class.getName());
        contextDir = testDir.resolve(contextName);
        FS.ensureEmpty(contextDir);

        // Ensure WEB-INF directories.
        webInf = contextDir.resolve("WEB-INF");
        FS.ensureDirExists(webInf);
        classesDir = webInf.resolve("classes");
        FS.ensureDirExists(classesDir);

        // Configure the WebAppContext.
        setContextPath("/" + contextName);
        setBaseResource(new PathResource(contextDir));
        addConfiguration(new JettyWebSocketConfiguration());
    }

    public Path getContextDir()
    {
        return contextDir;
    }

    public void createWebXml() throws IOException
    {
        String emptyWebXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<web-app xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://java.sun.com/xml/ns/javaee\" " +
            "xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd\" " +
            "metadata-complete=\"false\" version=\"3.0\"></web-app>";

        Path webXml = webInf.resolve("web.xml");
        try (FileWriter writer = new FileWriter(webXml.toFile()))
        {
            writer.write(emptyWebXml);
        }
    }

    public void copyWebXml(Path webXml) throws IOException
    {
        IO.copy(webXml.toFile(), webInf.resolve("web.xml").toFile());
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
}