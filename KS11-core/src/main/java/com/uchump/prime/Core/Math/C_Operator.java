package com.uchump.prime.Core.Math;

import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import java.util.Comparator;
import java.util.function.Function;

public class C_Operator {

	// comparison

	public static <T, U extends Comparable<? super U>> Comparator compareBy(
			Function<? super T, ? extends U> keyExtractor) {
		return Comparator.comparing(keyExtractor);
	}

	public static <T, U> Comparator<T> compareBy(Function<? super T, ? extends U> keyExtractor,
			Comparator<? super U> keyComparator) {
		return Comparator.comparing(keyExtractor, keyComparator);

	}
}
