package com.uchump.prime.Metatron.Lib._HTTP._Spark.ssl;

/**
 * SSL Stores
 */
public class SslStores {

    protected String keystoreFile;
    protected String keystorePassword;
    protected String certAlias;
    protected String truststoreFile;
    protected String truststorePassword;
    protected boolean needsClientCert;

    /**
     * Creates a Stores instance.
     *
     * @param keystoreFile the keystoreFile
     * @param keystorePassword the keystorePassword
     * @param truststoreFile the truststoreFile
     * @param truststorePassword the truststorePassword
     * @return the SslStores instance.
     */
    public static SslStores create(String keystoreFile,
                                String keystorePassword,
                                String truststoreFile,
                                String truststorePassword) {

        return new SslStores(keystoreFile, keystorePassword, null, truststoreFile, truststorePassword, false);
    }

    public static SslStores create(String keystoreFile,
                                String keystorePassword,
                                String certAlias,
                                String truststoreFile,
                                String truststorePassword) {

        return new SslStores(keystoreFile, keystorePassword, certAlias, truststoreFile, truststorePassword, false);
    }

    public static SslStores create(String keystoreFile,
                                   String keystorePassword,
                                   String truststoreFile,
                                   String truststorePassword,
                                   boolean needsClientCert) {

        return new SslStores(keystoreFile, keystorePassword, null, truststoreFile, truststorePassword, needsClientCert);
    }

    public static SslStores create(String keystoreFile,
                                   String keystorePassword,
                                   String certAlias,
                                   String truststoreFile,
                                   String truststorePassword,
                                   boolean needsClientCert) {

        return new SslStores(keystoreFile, keystorePassword, certAlias, truststoreFile, truststorePassword, needsClientCert);
    }

    private SslStores(String keystoreFile,
                      String keystorePassword,
                      String certAlias,
                      String truststoreFile,
                      String truststorePassword,
                      boolean needsClientCert) {
        this.keystoreFile = keystoreFile;
        this.keystorePassword = keystorePassword;
        this.certAlias = certAlias;
        this.truststoreFile = truststoreFile;
        this.truststorePassword = truststorePassword;
        this.needsClientCert = needsClientCert;
    }

    /**
     * @return keystoreFile
     */
    public String keystoreFile() {
        return keystoreFile;
    }

    /**
     * @return keystorePassword
     */
    public String keystorePassword() {
        return keystorePassword;
    }

    /**
     * @return certAlias
     */
    public String certAlias() {
        return certAlias;
    }

    /**
     * @return trustStoreFile
     */
    public String trustStoreFile() {
        return truststoreFile;
    }

    /**
     * @return trustStorePassword
     */
    public String trustStorePassword() {
        return truststorePassword;
    }

    /**
     * @return needsClientCert
     */
    public boolean needsClientCert() {
        return needsClientCert;
    }
}