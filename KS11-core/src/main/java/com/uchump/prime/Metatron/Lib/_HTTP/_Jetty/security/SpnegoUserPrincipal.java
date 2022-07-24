package com.uchump.prime.Metatron.Lib._HTTP._Jetty.security;
import java.security.Principal;
import java.util.Base64;

public class SpnegoUserPrincipal implements Principal
{
    private final String _name;
    private byte[] _token;
    private String _encodedToken;

    public SpnegoUserPrincipal(String name, String encodedToken)
    {
        _name = name;
        _encodedToken = encodedToken;
    }

    public SpnegoUserPrincipal(String name, byte[] token)
    {
        _name = name;
        _token = token;
    }

    @Override
    public String getName()
    {
        return _name;
    }

    public byte[] getToken()
    {
        if (_token == null)
            _token = Base64.getDecoder().decode(_encodedToken);
        return _token;
    }

    public String getEncodedToken()
    {
        if (_encodedToken == null)
            _encodedToken = new String(Base64.getEncoder().encode(_token));
        return _encodedToken;
    }
}