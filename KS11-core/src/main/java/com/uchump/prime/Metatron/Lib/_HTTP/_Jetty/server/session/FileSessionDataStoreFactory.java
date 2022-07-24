package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.session;

import java.io.File;

/**
 * FileSessionDataStoreFactory
 */
public class FileSessionDataStoreFactory extends AbstractSessionDataStoreFactory
{
    boolean _deleteUnrestorableFiles;
    File _storeDir;

    /**
     * @return the deleteUnrestorableFiles
     */
    public boolean isDeleteUnrestorableFiles()
    {
        return _deleteUnrestorableFiles;
    }

    /**
     * @param deleteUnrestorableFiles the deleteUnrestorableFiles to set
     */
    public void setDeleteUnrestorableFiles(boolean deleteUnrestorableFiles)
    {
        _deleteUnrestorableFiles = deleteUnrestorableFiles;
    }

    /**
     * @return the storeDir
     */
    public File getStoreDir()
    {
        return _storeDir;
    }

    /**
     * @param storeDir the storeDir to set
     */
    public void setStoreDir(File storeDir)
    {
        _storeDir = storeDir;
    }

    @Override
    public SessionDataStore getSessionDataStore(SessionHandler handler)
    {
        FileSessionDataStore fsds = new FileSessionDataStore();
        fsds.setDeleteUnrestorableFiles(isDeleteUnrestorableFiles());
        fsds.setStoreDir(getStoreDir());
        fsds.setGracePeriodSec(getGracePeriodSec());
        fsds.setSavePeriodSec(getSavePeriodSec());
        return fsds;
    }
}