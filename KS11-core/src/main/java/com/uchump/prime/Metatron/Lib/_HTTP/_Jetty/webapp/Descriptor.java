package com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp;

import java.util.Objects;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.resource.Resource;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.xml.XmlParser;

public abstract class Descriptor
{
    protected Resource _xml;
    protected XmlParser.Node _root;
    protected String _dtd;

    public Descriptor(Resource xml)
    {
        _xml = Objects.requireNonNull(xml);
    }

    public void parse(XmlParser parser)
        throws Exception
    {

        if (_root == null)
        {
            Objects.requireNonNull(parser);
            try
            {
                _root = parser.parse(_xml.getInputStream());
                _dtd = parser.getDTD();
            }
            finally
            {
                _xml.close();
            }
        }
    }

    public boolean isParsed()
    {
        return _root != null;
    }

    public Resource getResource()
    {
        return _xml;
    }

    public XmlParser.Node getRoot()
    {
        return _root;
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "(" + _xml + ")";
    }
}