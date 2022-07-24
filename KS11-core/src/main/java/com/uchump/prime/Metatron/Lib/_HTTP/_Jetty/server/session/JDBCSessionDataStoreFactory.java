package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.session;
/**
 * JDBCSessionDataStoreFactory
 */
public class JDBCSessionDataStoreFactory extends AbstractSessionDataStoreFactory
{

    /**
     *
     */
    DatabaseAdaptor _adaptor;

    /**
     *
     */
    JDBCSessionDataStore.SessionTableSchema _schema;

    @Override
    public SessionDataStore getSessionDataStore(SessionHandler handler)
    {
        JDBCSessionDataStore ds = new JDBCSessionDataStore();
        ds.setDatabaseAdaptor(_adaptor);
        ds.setSessionTableSchema(_schema);
        ds.setGracePeriodSec(getGracePeriodSec());
        ds.setSavePeriodSec(getSavePeriodSec());
        return ds;
    }

    /**
     * @param adaptor the {@link DatabaseAdaptor} to set
     */
    public void setDatabaseAdaptor(DatabaseAdaptor adaptor)
    {
        _adaptor = adaptor;
    }

    /**
     * @param schema the {@link JDBCSessionDataStoreFactory} to set
     */
    public void setSessionTableSchema(JDBCSessionDataStore.SessionTableSchema schema)
    {
        _schema = schema;
    }
}