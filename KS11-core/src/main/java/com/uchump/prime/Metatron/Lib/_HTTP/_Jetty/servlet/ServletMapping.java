package com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet;


import java.io.IOException;
import java.util.Arrays;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedAttribute;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedObject;

@ManagedObject("Servlet Mapping")
public class ServletMapping
{
    private String[] _pathSpecs;
    private String _servletName;
    private boolean _default;
    private Source _source;

    public ServletMapping()
    {
        this(Source.EMBEDDED);
    }

    public ServletMapping(Source source)
    {
        _source = source;
    }

    /**
     * @return Returns the pathSpecs.
     */
    @ManagedAttribute(value = "url patterns", readonly = true)
    public String[] getPathSpecs()
    {
        return _pathSpecs;
    }

    /**
     * @return Returns the servletName.
     */
    @ManagedAttribute(value = "servlet name", readonly = true)
    public String getServletName()
    {
        return _servletName;
    }

    /**
     * @param pathSpecs The pathSpecs to set.
     */
    public void setPathSpecs(String[] pathSpecs)
    {
        _pathSpecs = pathSpecs;
    }

    /**
     * Test if the list of path specs contains a particular one.
     *
     * @param pathSpec the path spec
     * @return true if path spec matches something in mappings
     */
    public boolean containsPathSpec(String pathSpec)
    {
        if (_pathSpecs == null || _pathSpecs.length == 0)
            return false;

        for (String p : _pathSpecs)
        {
            if (p.equals(pathSpec))
                return true;
        }
        return false;
    }

    /**
     * @param pathSpec The pathSpec to set.
     */
    public void setPathSpec(String pathSpec)
    {
        _pathSpecs = new String[]{pathSpec};
    }

    /**
     * @param servletName The servletName to set.
     */
    public void setServletName(String servletName)
    {
        _servletName = servletName;
    }

    @ManagedAttribute(value = "default", readonly = true)
    public boolean isFromDefaultDescriptor()
    {
        return _default;
    }

    public void setFromDefaultDescriptor(boolean fromDefault)
    {
        _default = fromDefault;
    }

    public Source getSource()
    {
        return _source;
    }

    @Override
    public String toString()
    {
        return (_pathSpecs == null ? "[]" : Arrays.asList(_pathSpecs).toString()) + "=>" + _servletName;
    }

    public void dump(Appendable out, String indent) throws IOException
    {
        out.append(String.valueOf(this)).append("\n");
    }
}