package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.extensions;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.ExtensionConfig;

import static com.uchump.prime.Metatron.Lib._Hamcrest.MatcherAssert.assertThat;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.is;
import static com.uchump.prime.Metatron.Lib._Hamcrest.Matchers.notNullValue;

public class ExtensionConfigTest
{
    private void assertConfig(ExtensionConfig cfg, String expectedName, Map<String, String> expectedParams)
    {
        String prefix = "ExtensionConfig";
        assertThat(prefix + ".Name", cfg.getName(), is(expectedName));

        prefix += ".getParameters()";
        Map<String, String> actualParams = cfg.getParameters();
        assertThat(prefix, actualParams, notNullValue());
        assertThat(prefix + ".size", actualParams.size(), is(expectedParams.size()));

        for (String expectedKey : expectedParams.keySet())
        {
            assertThat(prefix + ".containsKey(" + expectedKey + ")", actualParams.containsKey(expectedKey), is(true));

            String expectedValue = expectedParams.get(expectedKey);
            String actualValue = actualParams.get(expectedKey);

            assertThat(prefix + ".containsKey(" + expectedKey + ")", actualValue, is(expectedValue));
        }
    }

    @Test
    public void testParseMuxExample()
    {
        ExtensionConfig cfg = ExtensionConfig.parse("mux; max-channels=4; flow-control");
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("max-channels", "4");
        expectedParams.put("flow-control", null);
        assertConfig(cfg, "mux", expectedParams);
    }

    @Test
    public void testParsePerMessageCompressExample1()
    {
        ExtensionConfig cfg = ExtensionConfig.parse("permessage-compress; method=foo");
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("method", "foo");
        assertConfig(cfg, "permessage-compress", expectedParams);
    }

    @Test
    public void testParsePerMessageCompressExample2()
    {
        ExtensionConfig cfg = ExtensionConfig.parse("permessage-compress; method=\"foo; x=10\"");
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("method", "foo; x=10");
        assertConfig(cfg, "permessage-compress", expectedParams);
    }

    @Test
    public void testParsePerMessageCompressExample3()
    {
        ExtensionConfig cfg = ExtensionConfig.parse("permessage-compress; method=\"foo, bar\"");
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("method", "foo, bar");
        assertConfig(cfg, "permessage-compress", expectedParams);
    }

    @Test
    public void testParsePerMessageCompressExample4()
    {
        ExtensionConfig cfg = ExtensionConfig.parse("permessage-compress; method=\"foo; use_x, foo\"");
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("method", "foo; use_x, foo");
        assertConfig(cfg, "permessage-compress", expectedParams);
    }

    @Test
    public void testParsePerMessageCompressExample5()
    {
        ExtensionConfig cfg = ExtensionConfig.parse("permessage-compress; method=\"foo; x=\\\"Hello World\\\", bar\"");
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("method", "foo; x=\"Hello World\", bar");
        assertConfig(cfg, "permessage-compress", expectedParams);
    }

    @Test
    public void testParseSimpleBasicParameters()
    {
        ExtensionConfig cfg = ExtensionConfig.parse("bar; baz=2");
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("baz", "2");
        assertConfig(cfg, "bar", expectedParams);
    }

    @Test
    public void testParseSimpleNoParameters()
    {
        ExtensionConfig cfg = ExtensionConfig.parse("foo");
        Map<String, String> expectedParams = new HashMap<>();
        assertConfig(cfg, "foo", expectedParams);
    }
}