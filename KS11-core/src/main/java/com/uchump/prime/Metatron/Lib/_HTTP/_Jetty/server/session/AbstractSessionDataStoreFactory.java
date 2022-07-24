package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.session;

/**
 * AbstractSessionDataStoreFactory
 */
public abstract class AbstractSessionDataStoreFactory implements SessionDataStoreFactory
{

    int _gracePeriodSec = AbstractSessionDataStore.DEFAULT_GRACE_PERIOD_SEC;
    int _savePeriodSec = AbstractSessionDataStore.DEFAULT_SAVE_PERIOD_SEC;

    /**
     * @return the gracePeriodSec
     */
    public int getGracePeriodSec()
    {
        return _gracePeriodSec;
    }

    /**
     * @param gracePeriodSec the gracePeriodSec to set
     */
    public void setGracePeriodSec(int gracePeriodSec)
    {
        _gracePeriodSec = gracePeriodSec;
    }

    /**
     * @return the savePeriodSec
     */
    public int getSavePeriodSec()
    {
        return _savePeriodSec;
    }

    /**
     * @param savePeriodSec the savePeriodSec to set
     */
    public void setSavePeriodSec(int savePeriodSec)
    {
        _savePeriodSec = savePeriodSec;
    }
}