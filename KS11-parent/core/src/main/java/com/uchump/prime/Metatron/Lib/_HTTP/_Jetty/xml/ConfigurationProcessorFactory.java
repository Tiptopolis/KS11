package com.uchump.prime.Metatron.Lib._HTTP._Jetty.xml;
public interface ConfigurationProcessorFactory
{
    ConfigurationProcessor getConfigurationProcessor(String dtd, String tag);
}