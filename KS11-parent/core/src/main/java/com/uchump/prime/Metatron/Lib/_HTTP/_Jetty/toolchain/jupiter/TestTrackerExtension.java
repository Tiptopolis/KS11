package com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain.jupiter;

import java.lang.reflect.Method;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain.StringMangler;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TestTrackerExtension implements BeforeEachCallback
{
    @Override
    public void beforeEach(ExtensionContext extensionContext)
    {
        Boolean logDisplay = Boolean.getBoolean("jetty.testtracker.log");
        if (logDisplay)
        {
            Method method = extensionContext.getRequiredTestMethod();
            Class<?> clazz = extensionContext.getRequiredTestClass();
            // until junit issue fixed https://github.com/junit-team/junit5/issues/1139
            // we cannot get argument values so use a mix with displayName for methods with args
            if (method.getParameterCount() > 0)
            {
                String displayName = extensionContext.getDisplayName();
                if (displayName.contains(method.getName()))
                {
                    // this display name contains the method.
                    System.err.printf("Running %s.%s%n",
                        clazz.getName(),
                        StringMangler.escapeJava(displayName));
                }
                else
                {
                    // this display name does not contain method name, so include it.
                    System.err.printf("Running %s.%s(%s)%n",
                        clazz.getName(),
                        method.getName(),
                        StringMangler.escapeJava(displayName));
                }
            }
            else
            {
                // this one has no parameters
                System.err.printf("Running %s.%s()%n",
                    clazz.getName(),
                    method.getName());
            }
        }
    }
}