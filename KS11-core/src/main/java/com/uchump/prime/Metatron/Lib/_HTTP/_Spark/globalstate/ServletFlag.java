package com.uchump.prime.Metatron.Lib._HTTP._Spark.globalstate;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Holds the global information if Spark was run from an "external" web application server.
 */
public class ServletFlag {

    private static AtomicBoolean isRunningFromServlet = new AtomicBoolean(false);

    /**
     * Tells the system that Spark was run from an "external" web application server.
     */
    public static void runFromServlet() {
        isRunningFromServlet.set(true);
    }

    /**
     * @return true if Spark was run from an "external" web application server.
     */
    public static boolean isRunningFromServlet() {
        return isRunningFromServlet.get();
    }

}