package com.uchump.prime.Metatron.Lib._Hamcrest.core;

import com.uchump.prime.Metatron.Lib._Hamcrest.Matcher;

public final class CombinableBothMatcher<X> {
		private final Matcher<X> first;

		public CombinableBothMatcher(Matcher<X> matcher) {
			this.first = matcher;
		}

		public CombinableMatcher<X> and(Matcher<X> other) {
			return new CombinableMatcher<>(first).and(other);
		}
	}