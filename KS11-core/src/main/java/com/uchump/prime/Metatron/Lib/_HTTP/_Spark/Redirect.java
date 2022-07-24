package com.uchump.prime.Metatron.Lib._HTTP._Spark;
/**
 * Provides redirect utility methods.
 */
public final class Redirect {

    /**
     * The available redirect status codes.
     */
    public enum Status {
        MULTIPLE_CHOICES(300),
        MOVED_PERMANENTLY(301),
        FOUND(302),
        SEE_OTHER(303),
        NOT_MODIFIED(304),
        USE_PROXY(305),
        SWITCH_PROXY(306),
        TEMPORARY_REDIRECT(307),
        PERMANENT_REDIRECT(308);

        private int intValue;

        private Status(int intValue) {
            this.intValue = intValue;
        }

        public int intValue() {
            return intValue;
        }
    }

    /**
     * Creates a `Redirect` instance
     */
    static Redirect create(Routable http) {
        return new Redirect(http);
    }

    private Redirect(Routable http) {
        this.http = http;
    }

    private Routable http;

    /**
     * Redirects any HTTP request of type GET, POST, PUT, DELETE on 'fromPath' to 'toPath'
     *
     * @param fromPath from path
     * @param toPath   to path
     */
    public void any(String fromPath, String toPath) {
        any(fromPath, toPath, null);
    }

    /**
     * Redirects any HTTP request of type GET on 'fromPath' to 'toPath'
     *
     * @param fromPath from path
     * @param toPath   to path
     */
    public void get(String fromPath, String toPath) {
        get(fromPath, toPath, null);
    }

    /**
     * Redirects any HTTP request of type POST on 'fromPath' to 'toPath'
     *
     * @param fromPath from path
     * @param toPath   to path
     */
    public void post(String fromPath, String toPath) {
        post(fromPath, toPath, null);
    }

    /**
     * Redirects any HTTP request of type PUT on 'fromPath' to 'toPath'
     *
     * @param fromPath from path
     * @param toPath   to path
     */
    public void put(String fromPath, String toPath) {
        put(fromPath, toPath, null);
    }

    /**
     * Redirects any HTTP request of type DELETE on 'fromPath' to 'toPath'
     *
     * @param fromPath from path
     * @param toPath   to path
     */
    public void delete(String fromPath, String toPath) {
        delete(fromPath, toPath, null);
    }

    /**
     * Redirects any HTTP request of type GET, POST, PUT, DELETE on 'fromPath' to 'toPath' with the provided redirect
     * 'status' code.
     *
     * @param fromPath from path
     * @param toPath   to path
     * @param status   status code
     */
    public void any(String fromPath, String toPath, Status status) {
        get(fromPath, toPath, status);
        post(fromPath, toPath, status);
        put(fromPath, toPath, status);
        delete(fromPath, toPath, status);
    }

    /**
     * Redirects any HTTP request of type GET on 'fromPath' to 'toPath' with the provided redirect 'status' code.
     *
     * @param fromPath from path
     * @param toPath   to path
     * @param status   status code
     */
    public void get(String fromPath, String toPath, Status status) {
        http.get(fromPath, redirectRoute(toPath, status));
    }

    /**
     * Redirects any HTTP request of type POST on 'fromPath' to 'toPath' with the provided redirect 'status' code.
     *
     * @param fromPath from path
     * @param toPath   to path
     * @param status   status code
     */
    public void post(String fromPath, String toPath, Status status) {
        http.post(fromPath, redirectRoute(toPath, status));
    }

    /**
     * Redirects any HTTP request of type PUT on 'fromPath' to 'toPath' with the provided redirect 'status' code.
     *
     * @param fromPath from path
     * @param toPath   to path
     * @param status   status code
     */
    public void put(String fromPath, String toPath, Status status) {
        http.put(fromPath, redirectRoute(toPath, status));
    }

    /**
     * Redirects any HTTP request of type DELETE on 'fromPath' to 'toPath' with the provided redirect 'status' code.
     *
     * @param fromPath from path
     * @param toPath   to path
     * @param status   status code
     */
    public void delete(String fromPath, String toPath, Status status) {
        http.delete(fromPath, redirectRoute(toPath, status));
    }

    private static Route redirectRoute(String toPath, Status status) {
        return (req, res) -> {
            if (status != null) {
                res.redirect(toPath, status.intValue());
            } else {
                res.redirect(toPath);
            }
            return null;
        };
    }

}