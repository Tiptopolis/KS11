package com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations.AnnotationParser.AbstractHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.annotations.AnnotationParser.ClassInfo;

/**
 * ClassInheritanceHandler
 *
 * As asm scans for classes, remember the type hierarchy.
 */
public class ClassInheritanceHandler extends AbstractHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(ClassInheritanceHandler.class);

    Map<String, Set<String>> _inheritanceMap;

    public ClassInheritanceHandler(Map<String, Set<String>> map)
    {
        _inheritanceMap = map;
    }

    @Override
    public void handle(ClassInfo classInfo)
    {
        try
        {
            //Don't scan Object
            if ("java.lang.Object".equals(classInfo.getClassName()))
                return;

            for (int i = 0; classInfo.getInterfaces() != null && i < classInfo.getInterfaces().length; i++)
            {
                addToInheritanceMap(classInfo.getInterfaces()[i], classInfo.getClassName());
            }
            //To save memory, we don't record classes that only extend Object, as that can be assumed
            if (!"java.lang.Object".equals(classInfo.getSuperName()))
            {
                addToInheritanceMap(classInfo.getSuperName(), classInfo.getClassName());
            }
        }
        catch (Exception e)
        {
            LOG.warn("Failed to handle {}", classInfo, e);
        }
    }

    private void addToInheritanceMap(String interfaceOrSuperClassName, String implementingOrExtendingClassName)
    {

        //As it is likely that the interfaceOrSuperClassName is already in the map, try getting it first
        Set<String> implementingClasses = _inheritanceMap.get(interfaceOrSuperClassName);
        //If it isn't in the map, then add it in, but test to make sure that someone else didn't get in 
        //first and add it
        if (implementingClasses == null)
        {
            implementingClasses = ConcurrentHashMap.newKeySet();
            Set<String> tmp = _inheritanceMap.putIfAbsent(interfaceOrSuperClassName, implementingClasses);
            if (tmp != null)
                implementingClasses = tmp;
        }

        implementingClasses.add(implementingOrExtendingClassName);
    }
}