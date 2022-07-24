package com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.pathmap;
public interface MatchedPath
{
    MatchedPath EMPTY = new MatchedPath()
    {
        @Override
        public String getPathMatch()
        {
            return null;
        }

        @Override
        public String getPathInfo()
        {
            return null;
        }

        @Override
        public String toString()
        {
            return MatchedPath.class.getSimpleName() + ".EMPTY";
        }
    };

    static MatchedPath from(String pathMatch, String pathInfo)
    {
        return new MatchedPath()
        {
            @Override
            public String getPathMatch()
            {
                return pathMatch;
            }

            @Override
            public String getPathInfo()
            {
                return pathInfo;
            }

            @Override
            public String toString()
            {
                return MatchedPath.class.getSimpleName() + "[pathMatch=" + pathMatch + ", pathInfo=" + pathInfo + "]";
            }
        };
    }

    /**
     * Return the portion of the path that matches a path spec.
     *
     * @return the path name portion of the match.
     */
    String getPathMatch();

    /**
     * Return the portion of the path that is after the path spec.
     *
     * @return the path info portion of the match, or null if there is no portion after the {@link #getPathMatch()}
     */
    String getPathInfo();
}