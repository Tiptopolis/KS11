package com.uchump.prime.Metatron.Lib._Hamcrest.core;

import java.util.ArrayList;

import com.uchump.prime.Metatron.Lib._Hamcrest.Description;
import com.uchump.prime.Metatron.Lib._Hamcrest.Matcher;
import com.uchump.prime.Metatron.Lib._Hamcrest.TypeSafeDiagnosingMatcher;

/**
 * TODO: Finish Class Level Documentation.
 *
 * @param <T> the type of matcher being combined.
 */
public class CombinableMatcher<T> extends TypeSafeDiagnosingMatcher<T> {

	private final Matcher<? super T> matcher;

	public CombinableMatcher(Matcher<? super T> matcher) {
		this.matcher = matcher;
	}

	@Override
	protected boolean matchesSafely(T item, Description mismatch) {
		if (!matcher.matches(item)) {
			matcher.describeMismatch(item, mismatch);
			return false;
		}
		return true;
	}

	@Override
	public void describeTo(Description description) {
		description.appendDescriptionOf(matcher);
	}

	public CombinableMatcher<T> and(Matcher<? super T> other) {
		return new CombinableMatcher<>(new AllOf<>(templatedListWith(other)));
	}

	public CombinableMatcher<T> or(Matcher<? super T> other) {
		return new CombinableMatcher<>(new AnyOf<>(templatedListWith(other)));
	}

	private ArrayList<Matcher<? super T>> templatedListWith(Matcher<? super T> other) {
		ArrayList<Matcher<? super T>> matchers = new ArrayList<>();
		matchers.add(matcher);
		matchers.add(other);
		return matchers;
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
	 * @param matcher the matcher to combine, and both must pass.
	 * @return The matcher.
	 */
	public static <LHS> CombinableBothMatcher<LHS> both(Matcher<LHS> matcher) {
		return new CombinableBothMatcher<LHS>(matcher);
	}

	public static final class CombinableBothMatcher<X> {
		private final Matcher<X> first;

		public CombinableBothMatcher(Matcher<X> matcher) {
			this.first = matcher;
		}

		public CombinableMatcher<X> and(Matcher<X> other) {
			return new CombinableMatcher<>(first).and(other);
		}
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
	public static <LHS> CombinableEitherMatcher<LHS> either(Matcher<LHS> matcher) {
		return new CombinableEitherMatcher<LHS>(matcher);
	}

	public static final class CombinableEitherMatcher<X> {
		private final Matcher<X> first;

		public CombinableEitherMatcher(Matcher<X> matcher) {
			this.first = matcher;
		}

		public CombinableMatcher<X> or(Matcher<X> other) {
			return new CombinableMatcher<>(first).or(other);
		}
	}

}