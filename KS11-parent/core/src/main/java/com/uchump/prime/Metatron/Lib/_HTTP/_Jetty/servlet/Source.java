package com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet;
/**
 * Source
 *
 * The source of a web artifact: servlet, filter, mapping etc
 */
public class Source
{
    public static final Source EMBEDDED = new Source(Origin.EMBEDDED, null);
    public static final Source JAVAX_API = new Source(Origin.JAVAX_API, null);

    public enum Origin
    {
        EMBEDDED, JAVAX_API, DESCRIPTOR, ANNOTATION
    }

    public Origin _origin;
    public String _resource;

    /**
     * @param o the Origin of the artifact (servlet, filter, mapping etc)
     * @param resource the location where the artifact was declared
     */
    public Source(Origin o, String resource)
    {
        if (o == null)
            throw new IllegalArgumentException("Origin is null");
        _origin = o;
        _resource = resource;
    }

    /**
     * @return the origin
     */
    public Origin getOrigin()
    {
        return _origin;
    }

    /**
     * @return the resource
     */
    public String getResource()
    {
        return _resource;
    }

    @Override
    public String toString()
    {

        return _origin + ":" + _resource;
    }
}