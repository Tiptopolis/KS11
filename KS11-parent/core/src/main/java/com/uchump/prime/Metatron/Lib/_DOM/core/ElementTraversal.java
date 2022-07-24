package com.uchump.prime.Metatron.Lib._DOM.core;

/**
 * The {@code ElementTraversal} interface is a set of read-only attributes
 * which allow an author to easily navigate between elements in a document.
 * <p>
 * In conforming implementations of Element Traversal, all objects that
 * implement {@link Element} must also implement the {@code ElementTraversal}
 * interface. Four of the methods,
 * {@link #getFirstElementChild}, {@link #getLastElementChild},
 * {@link #getPreviousElementSibling}, and {@link #getNextElementSibling},
 * each provides a live reference to another element with the defined
 * relationship to the current element, if the related element exists. The
 * fifth method, {@link #getChildElementCount}, exposes the number of child
 * elements of an element, for preprocessing before navigation.
 *
 * @see
 * <a href='http://www.w3.org/TR/ElementTraversal/'><cite>Element Traversal Specification</cite></a>
 *
 * @since 9
 */
public interface ElementTraversal {

    /**
     * Returns a reference to the first child node of the element which is of
     * the {@link Element} type.
     *
     * @return a reference to an element child, {@code null} if the element has
     * no child of the {@link Element} type.
     */
    Element getFirstElementChild();

    /**
     * Returns a reference to the last child node of the element which is of
     * the {@link Element} type.
     *
     * @return a reference to an element child, {@code null} if the element has
     * no child of the {@link Element} type.
     */
    Element getLastElementChild();

    /**
     * Returns a reference to the sibling node of the element which most immediately
     * precedes the element in document order, and which is of the {@link Element} type.
     *
     * @return a reference to an element child, {@code null} if the element has
     * no sibling node of the {@link Element} type that comes before this one.
     */
    Element getPreviousElementSibling();

    /**
     * Returns a reference to the sibling node of the element which most immediately
     * follows the element in document order, and which is of the {@link Element} type.
     *
     * @return a reference to an element child, {@code null} if the element has
     * no sibling node of the {@link Element} type that comes after this one.
     */
    Element getNextElementSibling();

    /**
     * Returns the current number of child nodes of the element which are of
     * the {@link Element} type.
     *
     * @return the number of element children, or {@code 0} if the element has
     * no element children.
     */
    int getChildElementCount();
}