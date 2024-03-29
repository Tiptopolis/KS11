package com.uchump.prime.Metatron.Lib._DOM.core.ranges;

/**
 * Range operations may throw a <code>RangeException</code> as specified in
 * their method descriptions.
 * <p>See also the <a href='http://www.w3.org/TR/2000/REC-DOM-Level-2-Traversal-Range-20001113'>Document Object Model (DOM) Level 2 Traversal and Range Specification</a>.
 * @since 9, DOM Level 2
 */
public class RangeException extends RuntimeException {
    private static final long serialVersionUID = 2427563372446661889L;

    public RangeException(short code, String message) {
       super(message);
       this.code = code;
    }
    public short   code;
    // RangeExceptionCode
    /**
     * If the boundary-points of a Range do not meet specific requirements.
     */
    public static final short BAD_BOUNDARYPOINTS_ERR    = 1;
    /**
     * If the container of an boundary-point of a Range is being set to either
     * a node of an invalid type or a node with an ancestor of an invalid
     * type.
     */
    public static final short INVALID_NODE_TYPE_ERR     = 2;

}