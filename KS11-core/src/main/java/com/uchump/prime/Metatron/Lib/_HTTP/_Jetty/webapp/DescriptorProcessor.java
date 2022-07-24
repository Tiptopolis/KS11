package com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp;
/**
 * DescriptorProcessor
 */
public interface DescriptorProcessor
{
    public void process(WebAppContext context, Descriptor descriptor) throws Exception;
}