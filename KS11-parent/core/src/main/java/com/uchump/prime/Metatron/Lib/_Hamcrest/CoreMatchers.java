package com.uchump.prime.Metatron.Lib._Hamcrest;

import com.uchump.prime.Metatron.Lib._Hamcrest.core.IsIterableContaining;

@SuppressWarnings("UnusedDeclaration")
public class CoreMatchers {

	/**
	 * Creates a matcher that matches if the examined object matches <b>ALL</b> of
	 * the specified matchers. For example:
	 * 
	 * <pre>
	 * assertThat("myValue", allOf(startsWith("my"), containsString("Val")))
	 * </pre>
	 *
	 * @param <T>      the matcher type.
	 * @param matchers all the matchers must pass.
	 * @return The matcher.
	 */
	public static <T> com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<T> allOf(
			java.lang.Iterable<com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<? super T>> matchers) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.AllOf.allOf(matchers);
	}

	/**
	 * Creates a matcher that matches if the examined object matches <b>ALL</b> of
	 * the specified matchers. For example:
	 * 
	 * <pre>
	 * assertThat("myValue", allOf(startsWith("my"), containsString("Val")))
	 * </pre>
	 *
	 * @param <T>      the matcher type.
	 * @param matchers all the matchers must pass.
	 * @return The matcher.
	 */
	@SafeVarargs
	public static <T> com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<T> allOf(
			com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<? super T>... matchers) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.AllOf.allOf(matchers);
	}

	/**
	 * Creates a matcher that matches if the examined object matches <b>ANY</b> of
	 * the specified matchers. For example:
	 * 
	 * <pre>
	 * assertThat("myValue", anyOf(startsWith("foo"), containsString("Val")))
	 * </pre>
	 *
	 * @param <T>      the matcher type.
	 * @param matchers any the matchers must pass.
	 * @return The matcher.
	 */
	public static <T> com.uchump.prime.Metatron.Lib._Hamcrest.core.AnyOf<T> anyOf(
			java.lang.Iterable<com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<? super T>> matchers) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.AnyOf.anyOf(matchers);
	}

	/**
	 * Creates a matcher that matches if the examined object matches <b>ANY</b> of
	 * the specified matchers. For example:
	 * 
	 * <pre>
	 * assertThat("myValue", anyOf(startsWith("foo"), containsString("Val")))
	 * </pre>
	 *
	 * @param <T>      the matcher type.
	 * @param matchers any the matchers must pass.
	 * @return The matcher.
	 */
	@SafeVarargs
	public static <T> com.uchump.prime.Metatron.Lib._Hamcrest.core.AnyOf<T> anyOf(
			com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<? super T>... matchers) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.AnyOf.anyOf(matchers);
	}

	/**
	 * Creates a matcher that matches when both of the specified matchers match the
	 * examined object. For example:
	 * 
	 * <pre>
	 * assertThat("fab", both(containsString("a")).and(containsString("b")))
	 * </pre>
	 *
	 * @param <LHS>   the matcher type.
	 * @param matcher the matcher to combine, and both musth pass.
	 * @return The matcher.
	 */
	public static <LHS> com.uchump.prime.Metatron.Lib._Hamcrest.core.CombinableMatcher.CombinableBothMatcher<LHS> both(
			com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<LHS> matcher) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.CombinableMatcher.both(matcher);
	}

	/**
	 * Creates a matcher that matches when either of the specified matchers match
	 * the examined object. For example:
	 * 
	 * <pre>
	 * assertThat("fan", either(containsString("a")).or(containsString("b")))
	 * </pre>
	 *
	 * @param <LHS>   the matcher type.
	 * @param matcher the matcher to combine, and either must pass.
	 * @return The matcher.
	 */
	public static <LHS> com.uchump.prime.Metatron.Lib._Hamcrest.core.CombinableMatcher.CombinableEitherMatcher<LHS> either(
			com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<LHS> matcher) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.CombinableMatcher.either(matcher);
	}

	/**
	 * Wraps an existing matcher, overriding its description with that specified.
	 * All other functions are delegated to the decorated matcher, including its
	 * mismatch description. For example:
	 * 
	 * <pre>
	 * describedAs("a big decimal equal to %0", equalTo(myBigDecimal), myBigDecimal.toPlainString())
	 * </pre>
	 *
	 * @param <T>         the matcher type.
	 * @param description the new description for the wrapped matcher
	 * @param matcher     the matcher to wrap
	 * @param values      optional values to insert into the tokenised description
	 * @return The matcher.
	 */
	public static <T> com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<T> describedAs(java.lang.String description,
			com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<T> matcher, java.lang.Object... values) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.DescribedAs.describedAs(description, matcher, values);
	}

	/**
	 * Creates a matcher for {@link Iterable}s that only matches when a single pass
	 * over the examined {@link Iterable} yields items that are all matched by the
	 * specified <code>itemMatcher</code>. For example:
	 * 
	 * <pre>
	 * assertThat(Arrays.asList("bar", "baz"), everyItem(startsWith("ba")))
	 * </pre>
	 *
	 * @param <U>         the matcher type.
	 * @param itemMatcher the matcher to apply to every item provided by the
	 *                    examined {@link Iterable}
	 * @return The matcher.
	 */
	public static <U> com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<java.lang.Iterable<? extends U>> everyItem(
			com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<U> itemMatcher) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.Every.everyItem(itemMatcher);
	}

	/**
	 * Decorates another Matcher, retaining its behaviour, but allowing tests to be
	 * slightly more expressive. For example:
	 * 
	 * <pre>
	 * assertThat(cheese, is(equalTo(smelly)))
	 * </pre>
	 * 
	 * instead of:
	 * 
	 * <pre>
	 * assertThat(cheese, equalTo(smelly))
	 * </pre>
	 *
	 * @param <T>     the matcher type.
	 * @param matcher the matcher {@link org.hamcrest.core.Is#is}.
	 * @return The matcher.
	 */
	public static <T> com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<T> is(
			com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<T> matcher) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.Is.is(matcher);
	}

	/**
	 * A shortcut to the frequently used <code>is(equalTo(x))</code>. For example:
	 * 
	 * <pre>
	 * assertThat(cheese, is(smelly))
	 * </pre>
	 * 
	 * instead of:
	 * 
	 * <pre>
	 * assertThat(cheese, is(equalTo(smelly)))
	 * </pre>
	 *
	 * @param <T>   the matcher type.
	 * @param value the value for matcher {@link org.hamcrest.core.Is#is}.
	 * @return The matcher.
	 */
	public static <T> com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<T> is(T value) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.Is.is(value);
	}

	/**
	 * A shortcut to the frequently used
	 * <code>is(instanceOf(SomeClass.class))</code>. For example:
	 * 
	 * <pre>
	 * assertThat(cheese, isA(Cheddar.class))
	 * </pre>
	 * 
	 * instead of:
	 * 
	 * <pre>
	 * assertThat(cheese, is(instanceOf(Cheddar.class)))
	 * </pre>
	 *
	 * @param <T>  the matcher type.
	 * @param type the type for matcher {@link org.hamcrest.core.Is#isA}.
	 * @return The matcher.
	 */
	public static <T> com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<T> isA(java.lang.Class<T> type) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.Is.isA(type);
	}

	/**
	 * Creates a matcher that always matches, regardless of the examined object.
	 *
	 * @return The matcher.
	 */
	public static com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<java.lang.Object> anything() {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.IsAnything.anything();
	}

	/**
	 * Creates a matcher that always matches, regardless of the examined object, but
	 * describes itself with the specified {@link String}.
	 *
	 * @param description a meaningful {@link String} used when describing itself
	 * @return The matcher.
	 */
	public static com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<java.lang.Object> anything(
			java.lang.String description) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.IsAnything.anything(description);
	}

	/**
	 * Creates a matcher for {@link Iterable}s that only matches when a single pass
	 * over the examined {@link Iterable} yields at least one item that is matched
	 * by the specified <code>itemMatcher</code>. Whilst matching, the traversal of
	 * the examined {@link Iterable} will stop as soon as a matching item is found.
	 * For example:
	 * 
	 * <pre>
	 * assertThat(Arrays.asList("foo", "bar"), hasItem(startsWith("ba")))
	 * </pre>
	 *
	 * @param <T>         the matcher type.
	 * @param itemMatcher the matcher to apply to items provided by the examined
	 *                    {@link Iterable}
	 * @return The matcher.
	 */
	public static <T> com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<java.lang.Iterable<? super T>> hasItem(
			com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<? super T> itemMatcher) {
		return IsIterableContaining.hasItem(itemMatcher);
	}

	/**
	 * Creates a matcher for {@link Iterable}s that only matches when a single pass
	 * over the examined {@link Iterable} yields at least one item that is equal to
	 * the specified <code>item</code>. Whilst matching, the traversal of the
	 * examined {@link Iterable} will stop as soon as a matching item is found. For
	 * example:
	 * 
	 * <pre>
	 * assertThat(Arrays.asList("foo", "bar"), hasItem("bar"))
	 * </pre>
	 *
	 * @param <T>  the matcher type.
	 * @param item the item to compare against the items provided by the examined
	 *             {@link Iterable}
	 * @return The matcher.
	 */
	public static <T> com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<java.lang.Iterable<? super T>> hasItem(T item) {
		return IsIterableContaining.hasItem(item);
	}

	/**
	 * Creates a matcher for {@link Iterable}s that matches when consecutive passes
	 * over the examined {@link Iterable} yield at least one item that is matched by
	 * the corresponding matcher from the specified <code>itemMatchers</code>.
	 * Whilst matching, each traversal of the examined {@link Iterable} will stop as
	 * soon as a matching item is found. For example:
	 * 
	 * <pre>
	 * assertThat(Arrays.asList("foo", "bar", "baz"), hasItems(endsWith("z"), endsWith("o")))
	 * </pre>
	 *
	 * @param <T>          the matcher type.
	 * @param itemMatchers the matchers to apply to items provided by the examined
	 *                     {@link Iterable}
	 * @return The matcher.
	 */
	@SafeVarargs
	public static <T> com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<java.lang.Iterable<T>> hasItems(
			com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<? super T>... itemMatchers) {
		return IsIterableContaining.hasItems(itemMatchers);
	}

	/**
	 * Creates a matcher for {@link Iterable}s that matches when consecutive passes
	 * over the examined {@link Iterable} yield at least one item that is equal to
	 * the corresponding item from the specified <code>items</code>. Whilst
	 * matching, each traversal of the examined {@link Iterable} will stop as soon
	 * as a matching item is found. For example:
	 * 
	 * <pre>
	 * assertThat(Arrays.asList("foo", "bar", "baz"), hasItems("baz", "foo"))
	 * </pre>
	 *
	 * @param <T>   the matcher type.
	 * @param items the items to compare against the items provided by the examined
	 *              {@link Iterable}
	 * @return The matcher.
	 */
	@SafeVarargs
	public static <T> com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<java.lang.Iterable<T>> hasItems(T... items) {
		return IsIterableContaining.hasItems(items);
	}

	/**
	 * Creates a matcher that matches when the examined object is logically equal to
	 * the specified <code>operand</code>, as determined by calling the
	 * {@link java.lang.Object#equals} method on the <b>examined</b> object.
	 *
	 * <p>
	 * If the specified operand is <code>null</code> then the created matcher will
	 * only match if the examined object's <code>equals</code> method returns
	 * <code>true</code> when passed a <code>null</code> (which would be a violation
	 * of the <code>equals</code> contract), unless the examined object itself is
	 * <code>null</code>, in which case the matcher will return a positive match.
	 * </p>
	 *
	 * <p>
	 * The created matcher provides a special behaviour when examining
	 * <code>Array</code>s, whereby it will match if both the operand and the
	 * examined object are arrays of the same length and contain items that are
	 * equal to each other (according to the above rules) <b>in the same
	 * indexes</b>.
	 * </p>
	 * For example:
	 * 
	 * <pre>
	 * assertThat("foo", equalTo("foo"));
	 * assertThat(new String[] { "foo", "bar" }, equalTo(new String[] { "foo", "bar" }));
	 * </pre>
	 *
	 * @param <T>     the matcher type.
	 * @param operand for matcher {@link org.hamcrest.core.IsEqual#equalTo}.
	 * @return The matcher.
	 */
	public static <T> com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<T> equalTo(T operand) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.IsEqual.equalTo(operand);
	}

	/**
	 * Creates an {@link org.hamcrest.core.IsEqual} matcher that does not enforce
	 * the values being compared to be of the same static type.
	 *
	 * @param operand the object for matcher
	 *                {@link org.hamcrest.core.IsEqual#equalToObject}.
	 * @return The matcher.
	 */
	public static com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<java.lang.Object> equalToObject(
			java.lang.Object operand) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.IsEqual.equalToObject(operand);
	}

	/**
	 * Creates a matcher that matches when the examined object is an instance of the
	 * specified <code>type</code>, as determined by calling the
	 * {@link java.lang.Class#isInstance(Object)} method on that type, passing the
	 * the examined object.
	 *
	 * <p>
	 * The created matcher forces a relationship between specified type and the
	 * examined object, and should be used when it is necessary to make generics
	 * conform, for example in the JMock clause <code>with(any(Thing.class))</code>
	 * </p>
	 * For example:
	 * 
	 * <pre>
	 * assertThat(new Canoe(), any(Canoe.class));
	 * </pre>
	 *
	 * @param <T>  the matcher type.
	 * @param type the type for matcher {@link org.hamcrest.core.IsInstanceOf#any}.
	 * @return The matcher.
	 */
	public static <T> com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<T> any(java.lang.Class<T> type) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.IsInstanceOf.any(type);
	}

	/**
	 * Creates a matcher that matches when the examined object is an instance of the
	 * specified <code>type</code>, as determined by calling the
	 * {@link java.lang.Class#isInstance(Object)} method on that type, passing the
	 * the examined object.
	 *
	 * <p>
	 * The created matcher assumes no relationship between specified type and the
	 * examined object.
	 * </p>
	 * For example:
	 * 
	 * <pre>
	 * assertThat(new Canoe(), instanceOf(Paddlable.class));
	 * </pre>
	 *
	 * @param <T>  the matcher type.
	 * @param type the type for matcher
	 *             {@link org.hamcrest.core.IsInstanceOf#instanceOf}.
	 * @return The matcher.
	 */
	public static <T> com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<T> instanceOf(java.lang.Class<?> type) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.IsInstanceOf.instanceOf(type);
	}

	/**
	 * Creates a matcher that wraps an existing matcher, but inverts the logic by
	 * which it will match. For example:
	 * 
	 * <pre>
	 * assertThat(cheese, is(not(equalTo(smelly))))
	 * </pre>
	 *
	 * @param <T>     the matcher type.
	 * @param matcher the matcher whose sense should be inverted
	 * @return The matcher.
	 */
	public static <T> com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<T> not(
			com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<T> matcher) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.IsNot.not(matcher);
	}

	/**
	 * A shortcut to the frequently used <code>not(equalTo(x))</code>. For example:
	 * 
	 * <pre>
	 * assertThat(cheese, is(not(smelly)))
	 * </pre>
	 * 
	 * instead of:
	 * 
	 * <pre>
	 * assertThat(cheese, is(not(equalTo(smelly))))
	 * </pre>
	 *
	 * @param <T>   the matcher type.
	 * @param value the value that any examined object should <b>not</b> equal
	 * @return The matcher.
	 */
	public static <T> com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<T> not(T value) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.IsNot.not(value);
	}

	/**
	 * A shortcut to the frequently used <code>not(nullValue())</code>. For example:
	 * 
	 * <pre>
	 * assertThat(cheese, is(notNullValue()))
	 * </pre>
	 * 
	 * instead of:
	 * 
	 * <pre>
	 * assertThat(cheese, is(not(nullValue())))
	 * </pre>
	 *
	 * @return The matcher.
	 */
	public static com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<java.lang.Object> notNullValue() {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.IsNull.notNullValue();
	}

	/**
	 * A shortcut to the frequently used <code>not(nullValue(X.class)). Accepts a
	 * single dummy argument to facilitate type inference.</code>. For example:
	 * 
	 * <pre>
	 * assertThat(cheese, is(notNullValue(X.class)))
	 * </pre>
	 * 
	 * instead of:
	 * 
	 * <pre>
	 * assertThat(cheese, is(not(nullValue(X.class))))
	 * </pre>
	 *
	 * @param <T>  the matcher type.
	 * @param type dummy parameter used to infer the generic type of the returned
	 *             matcher
	 * @return The matcher.
	 */
	public static <T> com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<T> notNullValue(java.lang.Class<T> type) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.IsNull.notNullValue(type);
	}

	/**
	 * Creates a matcher that matches if examined object is <code>null</code>. For
	 * example:
	 * 
	 * <pre>
	 * assertThat(cheese, is(nullValue())
	 * </pre>
	 *
	 * @return The matcher.
	 */
	public static com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<java.lang.Object> nullValue() {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.IsNull.nullValue();
	}

	/**
	 * Creates a matcher that matches if examined object is <code>null</code>.
	 * Accepts a single dummy argument to facilitate type inference. For example:
	 * 
	 * <pre>
	 * assertThat(cheese, is(nullValue(Cheese.class))
	 * </pre>
	 *
	 * @param <T>  the matcher type.
	 * @param type dummy parameter used to infer the generic type of the returned
	 *             matcher
	 * @return The matcher.
	 */
	public static <T> com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<T> nullValue(java.lang.Class<T> type) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.IsNull.nullValue(type);
	}

	/**
	 * Creates a matcher that matches only when the examined object is the same
	 * instance as the specified target object.
	 *
	 * @param <T>    the matcher type.
	 * @param target the target instance against which others should be assessed
	 * @return The matcher.
	 */
	public static <T> com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<T> sameInstance(T target) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.IsSame.sameInstance(target);
	}

	/**
	 * Creates a matcher that matches only when the examined object is the same
	 * instance as the specified target object.
	 *
	 * @param <T>    the matcher type.
	 * @param target the target instance against which others should be assessed
	 * @return The matcher.
	 */
	public static <T> com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<T> theInstance(T target) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.IsSame.theInstance(target);
	}

	/**
	 * Creates a matcher that matches if the examined {@link String} contains the
	 * specified {@link String} anywhere. For example:
	 * 
	 * <pre>
	 * assertThat("myStringOfNote", containsString("ring"))
	 * </pre>
	 *
	 * @param substring the substring that the returned matcher will expect to find
	 *                  within any examined string
	 * @return The matcher.
	 */
	public static com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<java.lang.String> containsString(
			java.lang.String substring) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.StringContains.containsString(substring);
	}

	/**
	 * Creates a matcher that matches if the examined {@link String} contains the
	 * specified {@link String} anywhere, ignoring case. For example:
	 * 
	 * <pre>
	 * assertThat("myStringOfNote", containsString("ring"))
	 * </pre>
	 *
	 * @param substring the substring that the returned matcher will expect to find
	 *                  within any examined string
	 * @return The matcher.
	 */
	public static com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<java.lang.String> containsStringIgnoringCase(
			java.lang.String substring) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.StringContains.containsStringIgnoringCase(substring);
	}

	/**
	 * <p>
	 * Creates a matcher that matches if the examined {@link String} starts with the
	 * specified {@link String}.
	 * </p>
	 * For example:
	 * 
	 * <pre>
	 * assertThat("myStringOfNote", startsWith("my"))
	 * </pre>
	 *
	 * @param prefix the substring that the returned matcher will expect at the
	 *               start of any examined string
	 * @return The matcher.
	 */
	public static com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<java.lang.String> startsWith(
			java.lang.String prefix) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.StringStartsWith.startsWith(prefix);
	}

	/**
	 * <p>
	 * Creates a matcher that matches if the examined {@link String} starts with the
	 * specified {@link String}, ignoring case
	 * </p>
	 * For example:
	 * 
	 * <pre>
	 * assertThat("myStringOfNote", startsWith("my"))
	 * </pre>
	 *
	 * @param prefix the substring that the returned matcher will expect at the
	 *               start of any examined string
	 * @return The matcher.
	 */
	public static com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<java.lang.String> startsWithIgnoringCase(
			java.lang.String prefix) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.StringStartsWith.startsWithIgnoringCase(prefix);
	}

	/**
	 * Creates a matcher that matches if the examined {@link String} ends with the
	 * specified {@link String}. For example:
	 * 
	 * <pre>
	 * assertThat("myStringOfNote", endsWith("Note"))
	 * </pre>
	 *
	 * @param suffix the substring that the returned matcher will expect at the end
	 *               of any examined string
	 * @return The matcher.
	 */
	public static com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<java.lang.String> endsWith(java.lang.String suffix) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.StringEndsWith.endsWith(suffix);
	}

	/**
	 * Creates a matcher that matches if the examined {@link String} ends with the
	 * specified {@link String}, ignoring case. For example:
	 * 
	 * <pre>
	 * assertThat("myStringOfNote", endsWith("Note"))
	 * </pre>
	 *
	 * @param suffix the substring that the returned matcher will expect at the end
	 *               of any examined string
	 * @return The matcher.
	 */
	public static com.uchump.prime.Metatron.Lib._Hamcrest.Matcher<java.lang.String> endsWithIgnoringCase(
			java.lang.String suffix) {
		return com.uchump.prime.Metatron.Lib._Hamcrest.core.StringEndsWith.endsWithIgnoringCase(suffix);
	}

}