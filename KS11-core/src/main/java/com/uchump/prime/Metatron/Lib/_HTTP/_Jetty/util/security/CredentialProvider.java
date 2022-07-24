package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.security;

/**
 * Provider of credentials, it converts a String into a credential if it starts with a given prefix
 */
public interface CredentialProvider
{
    /**
     * Get a credential from a String
     *
     * @param credential String representation of the credential
     * @return A Credential or Password instance.
     */
    Credential getCredential(String credential);

    /**
     * Get the prefix of the credential strings convertible into credentials
     *
     * @return prefix of the credential strings convertible into credentials
     */
    String getPrefix();
}