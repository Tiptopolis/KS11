package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.jmx;


import com.uchump.prime.Metatron.Lib._HTTP._Jetty.jmx.ObjectMBean;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.AbstractConnector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ConnectionFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedObject;

@ManagedObject("MBean Wrapper for Connectors")
public class AbstractConnectorMBean extends ObjectMBean
{
    final AbstractConnector _connector;

    public AbstractConnectorMBean(Object managedObject)
    {
        super(managedObject);
        _connector = (AbstractConnector)managedObject;
    }

    @Override
    public String getObjectContextBasis()
    {
        StringBuilder buffer = new StringBuilder();
        for (ConnectionFactory f : _connector.getConnectionFactories())
        {
            String protocol = f.getProtocol();
            if (protocol != null)
            {
                if (buffer.length() > 0)
                    buffer.append("|");
                buffer.append(protocol);
            }
        }

        return String.format("%s@%x", buffer.toString(), _connector.hashCode());
    }
}