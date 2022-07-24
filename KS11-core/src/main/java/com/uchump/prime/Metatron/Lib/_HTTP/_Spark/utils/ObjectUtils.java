package com.uchump.prime.Metatron.Lib._HTTP._Spark.utils;
/**
 * Miscellaneous object utility methods.
 * Mainly for internal use within the framework.
 * <p>Thanks to Alex Ruiz for contributing several enhancements to this class!
 *
 * @author Juergen Hoeller
 * @author Keith Donald
 * @author Rod Johnson
 * @author Rob Harrop
 * @author Chris Beams
 *         Code copied from Spring source. Modifications made (mostly removal of methods) by Per Wendel.
 */
public abstract class ObjectUtils {

    /**
     * Determine whether the given array is empty:
     * i.e. {@code null} or of zero length.
     *
     * @param array the array to check
     * @return if empty
     */
    public static boolean isEmpty(Object[] array) {
        return (array == null || array.length == 0);
    }

}