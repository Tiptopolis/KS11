package com.uchump.prime.Metatron.Lib._HTTP._Spark.utils;
import java.util.ArrayList;
import java.util.List;

/**
 * Some utility methods
 *
 * @author Per Wendel
 */
public final class SparkUtils {

    public static final String ALL_PATHS = "+/*paths";

    private SparkUtils() {
    }

    public static List<String> convertRouteToList(String route) {
        String[] pathArray = route.split("/");
        List<String> path = new ArrayList<>();
        for (String p : pathArray) {
            if (p.length() > 0) {
                path.add(p);
            }
        }
        return path;
    }

    public static boolean isParam(String routePart) {
        return routePart.startsWith(":");
    }

    public static boolean isSplat(String routePart) {
        return routePart.equals("*");
    }

}