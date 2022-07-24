package com.uchump.prime.Metatron.Lib._HTTP._Jetty.plus.annotation;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.ServletHolder;

/**
 * RunAsCollection
 * @deprecated class unused as of 9.4.28 due for removal in 10.0.0
 */
@Deprecated
public class RunAsCollection
{
    private static final Logger LOG = LoggerFactory.getLogger(RunAsCollection.class);

    public static final String RUNAS_COLLECTION = "org.eclipse.jetty.runAsCollection";
    private ConcurrentMap<String, RunAs> _runAsMap = new ConcurrentHashMap<String, RunAs>(); //map of classname to run-as

    public void add(RunAs runAs)
    {
        if ((runAs == null) || (runAs.getTargetClassName() == null))
            return;

        if (LOG.isDebugEnabled())
            LOG.debug("Adding run-as for class=" + runAs.getTargetClassName());
        RunAs prev = _runAsMap.putIfAbsent(runAs.getTargetClassName(), runAs);
        if (prev != null)
            LOG.warn("Run-As {} on class {} ignored, already run-as {}", runAs.getRoleName(), runAs.getTargetClassName(), prev.getRoleName());
    }

    public RunAs getRunAs(Object o)
    {
        if (o == null)
            return null;

        return (RunAs)_runAsMap.get(o.getClass().getName());
    }

    public void setRunAs(Object o)
    {
        if (o == null)
            return;

        if (!ServletHolder.class.isAssignableFrom(o.getClass()))
            return;

        RunAs runAs = (RunAs)_runAsMap.get(o.getClass().getName());
        if (runAs == null)
            return;

        runAs.setRunAs((ServletHolder)o);
    }
}