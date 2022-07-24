package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server;

import javax.servlet.http.HttpServletMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.MappingMatch;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.pathmap.MatchedPath;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.pathmap.PathSpec;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.pathmap.ServletPathSpec;

/**
 * Implementation of HttpServletMapping.
 *
 * Represents the application of a {@link ServletPathSpec} to a specific path
 * that resulted in a mapping to a {@link javax.servlet.Servlet}.
 * As well as supporting the standard {@link HttpServletMapping} methods, this
 * class also carries fields, which can be precomputed for the implementation
 * of {@link HttpServletRequest#getServletPath()} and
 * {@link HttpServletRequest#getPathInfo()}
 */
public class ServletPathMapping implements HttpServletMapping
{
    private final MappingMatch _mappingMatch;
    private final String _matchValue;
    private final String _pattern;
    private final String _servletName;
    private final String _servletPath;
    private final String _pathInfo;

    public ServletPathMapping(PathSpec pathSpec, String servletName, String pathInContext, MatchedPath matchedPath)
    {
        _servletName = (servletName == null ? "" : servletName);

        if (pathSpec == null)
        {
            _pattern = null;
            _mappingMatch = null;
            _matchValue = "";
            _servletPath = pathInContext;
            _pathInfo = null;
            return;
        }

        if (pathInContext == null)
        {
            _pattern = pathSpec.getDeclaration();
            _mappingMatch = null;
            _matchValue = "";
            _servletPath = "";
            _pathInfo = null;
            return;
        }

        // Path Spec types that are not ServletPathSpec
        if (!(pathSpec instanceof ServletPathSpec))
        {
            _pattern = pathSpec.getDeclaration();
            _mappingMatch = null;
            if (matchedPath != null)
            {
                _servletPath = matchedPath.getPathMatch();
                _pathInfo = matchedPath.getPathInfo();
            }
            else
            {
                _servletPath = pathInContext;
                _pathInfo = null;
            }
            _matchValue = _servletPath.substring(_servletPath.charAt(0) == '/' ? 1 : 0);
            return;
        }

        // from here down is ServletPathSpec behavior
        _pattern = pathSpec.getDeclaration();

        switch (pathSpec.getGroup())
        {
            case ROOT:
                _mappingMatch = MappingMatch.CONTEXT_ROOT;
                _matchValue = "";
                _servletPath = "";
                _pathInfo = "/";
                break;

            case DEFAULT:
                _mappingMatch = MappingMatch.DEFAULT;
                _matchValue = "";
                _servletPath = pathInContext;
                _pathInfo = null;
                break;

            case EXACT:
                _mappingMatch = MappingMatch.EXACT;
                _matchValue = _pattern.startsWith("/") ? _pattern.substring(1) : _pattern;
                _servletPath = _pattern;
                _pathInfo = null;
                break;

            case PREFIX_GLOB:
                _mappingMatch = MappingMatch.PATH;
                _servletPath = pathSpec.getPrefix();
                // TODO avoid the substring on the known servletPath!
                _matchValue = _servletPath.startsWith("/") ? _servletPath.substring(1) : _servletPath;
                _pathInfo = matchedPath != null ? matchedPath.getPathInfo() : null;
                break;

            case SUFFIX_GLOB:
                _mappingMatch = MappingMatch.EXTENSION;
                int dot = pathInContext.lastIndexOf('.');
                _matchValue = pathInContext.substring(pathInContext.startsWith("/") ? 1 : 0, dot);
                _servletPath = pathInContext;
                _pathInfo = null;
                break;

            case MIDDLE_GLOB:
            default:
                throw new IllegalStateException("ServletPathSpec of type MIDDLE_GLOB");
        }
    }

    public ServletPathMapping(PathSpec pathSpec, String servletName, String pathInContext)
    {
        this(pathSpec, servletName, pathInContext, null);
    }

    @Override
    public String getMatchValue()
    {
        return _matchValue;
    }

    @Override
    public String getPattern()
    {
        return _pattern;
    }

    @Override
    public String getServletName()
    {
        return _servletName;
    }

    @Override
    public MappingMatch getMappingMatch()
    {
        return _mappingMatch;
    }

    public String getServletPath()
    {
        return _servletPath;
    }

    public String getPathInfo()
    {
        return _pathInfo;
    }

    @Override
    public String toString()
    {
        return "ServletPathMapping{" +
            "matchValue=" + _matchValue +
            ", pattern=" + _pattern +
            ", servletName=" + _servletName +
            ", mappingMatch=" + _mappingMatch +
            ", servletPath=" + _servletPath +
            ", pathInfo=" + _pathInfo +
            "}";
    }
}