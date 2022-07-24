package com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp;

public enum Origin
{
    NotSet, WebXml, WebDefaults, WebOverride, WebFragment, Annotation, API;

    public static Origin of(Object o)
    {
        if (o == null)
            return null;
        if (o instanceof java.lang.annotation.Annotation)
            return Annotation;
        if (o instanceof FragmentDescriptor)
            return WebFragment;
        else if (o instanceof OverrideDescriptor)
            return WebOverride;
        else if (o instanceof DefaultsDescriptor)
            return WebDefaults;
        else if (o instanceof WebDescriptor)
            return WebXml;
        else
            return API;
    }
}