package com.uchump.prime.Metatron.Lib._HTTP._Jetty.jndi;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.Destroyable;

/**
 * Close a DataSource.
 * Some {@link DataSource}'s need to be close (eg. Atomikos).  This bean is a {@link Destroyable} and
 * may be added to any {@link ContainerLifeCycle} so that {@link #destroy()}
 * will be called.   The {@link #destroy()} method calls any no-arg method called "close" on the passed DataSource.
 */
public class DataSourceCloser implements Destroyable
{
    private static final Logger LOG = LoggerFactory.getLogger(DataSourceCloser.class);

    final DataSource _datasource;
    final String _shutdown;

    public DataSourceCloser(DataSource datasource)
    {
        if (datasource == null)
            throw new IllegalArgumentException();
        _datasource = datasource;
        _shutdown = null;
    }

    public DataSourceCloser(DataSource datasource, String shutdownSQL)
    {
        if (datasource == null)
            throw new IllegalArgumentException();
        _datasource = datasource;
        _shutdown = shutdownSQL;
    }

    @Override
    public void destroy()
    {
        try
        {
            if (_shutdown != null)
            {
                LOG.info("Shutdown datasource {}", _datasource);
                try (Connection connection = _datasource.getConnection();
                     Statement stmt = connection.createStatement())
                {
                    stmt.executeUpdate(_shutdown);
                }
            }
        }
        catch (Exception e)
        {
            LOG.warn("Unable to shutdown datasource {}", _datasource, e);
        }

        try
        {
            Method close = _datasource.getClass().getMethod("close", new Class[]{});
            LOG.info("Close datasource {}", _datasource);
            close.invoke(_datasource, new Object[]{});
        }
        catch (Exception e)
        {
            LOG.warn("Unable to close datasource {}", _datasource, e);
        }
    }
}