package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.ssl;

import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedKeyManager;

/**
 * <p>An {@link X509ExtendedKeyManager} that select a key with desired alias,
 * delegating other processing to a nested X509ExtendedKeyManager.</p>
 * <p>Can be used both with server and client sockets.</p>
 */
public class AliasedX509ExtendedKeyManager extends X509ExtendedKeyManager
{
    private final String _alias;
    private final X509ExtendedKeyManager _delegate;

    public AliasedX509ExtendedKeyManager(X509ExtendedKeyManager keyManager, String keyAlias)
    {
        _alias = keyAlias;
        _delegate = keyManager;
    }

    public X509ExtendedKeyManager getDelegate()
    {
        return _delegate;
    }

    @Override
    public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket)
    {
        if (_alias == null)
            return _delegate.chooseClientAlias(keyType, issuers, socket);

        for (String kt : keyType)
        {
            String[] aliases = _delegate.getClientAliases(kt, issuers);
            if (aliases != null)
            {
                for (String a : aliases)
                {
                    if (_alias.equals(a))
                        return _alias;
                }
            }
        }

        return null;
    }

    @Override
    public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket)
    {
        if (_alias == null)
            return _delegate.chooseServerAlias(keyType, issuers, socket);

        String[] aliases = _delegate.getServerAliases(keyType, issuers);
        if (aliases != null)
        {
            for (String a : aliases)
            {
                if (_alias.equals(a))
                    return _alias;
            }
        }

        return null;
    }

    @Override
    public String[] getClientAliases(String keyType, Principal[] issuers)
    {
        return _delegate.getClientAliases(keyType, issuers);
    }

    @Override
    public String[] getServerAliases(String keyType, Principal[] issuers)
    {
        return _delegate.getServerAliases(keyType, issuers);
    }

    @Override
    public X509Certificate[] getCertificateChain(String alias)
    {
        return _delegate.getCertificateChain(alias);
    }

    @Override
    public PrivateKey getPrivateKey(String alias)
    {
        return _delegate.getPrivateKey(alias);
    }

    @Override
    public String chooseEngineServerAlias(String keyType, Principal[] issuers, SSLEngine engine)
    {
        if (_alias == null)
            return _delegate.chooseEngineServerAlias(keyType, issuers, engine);

        String[] aliases = _delegate.getServerAliases(keyType, issuers);
        if (aliases != null)
        {
            for (String a : aliases)
            {
                if (_alias.equals(a))
                    return _alias;
            }
        }

        return null;
    }

    @Override
    public String chooseEngineClientAlias(String[] keyType, Principal[] issuers, SSLEngine engine)
    {
        if (_alias == null)
            return _delegate.chooseEngineClientAlias(keyType, issuers, engine);

        for (String kt : keyType)
        {
            String[] aliases = _delegate.getClientAliases(kt, issuers);
            if (aliases != null)
            {
                for (String a : aliases)
                {
                    if (_alias.equals(a))
                        return _alias;
                }
            }
        }

        return null;
    }
}