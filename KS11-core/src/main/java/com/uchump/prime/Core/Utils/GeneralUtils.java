package com.uchump.prime.Core.Utils;

import com.uchump.prime.Core.Primitive.aNode;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.Struct.aMultiMap;
import com.uchump.prime.Core.Primitive.Utils.aThingSampler;
import com.uchump.prime.Core.Primitive.Utils.aThingCounter;

public class GeneralUtils {

	public static <T> aMultiMap<T, Integer> Counter(T... things) {
		aThingCounter<T> t = new aThingCounter<T>(things);

		aMultiMap<T, Integer> res = new aMultiMap<T, Integer>();
		for (aMultiMap.Entry<T, aNode<Integer>> E : t.things)
			res.put(E.getKey(), E.getValue().get());

		return res;
	}

	public static <T> aThingCounter<T> ThingCounter(T... things) {
		return new aThingCounter<T>(things);
	}

	public static <T> aThingSampler.Random<T> RandomSampler(iCollection<T> things, int num) {
		return new aThingSampler.Random<T>(things, num);
	}

	public static <T> aThingSampler.Regular<T> RegularSampler(iCollection<T> things, int num) {
		return new aThingSampler.Regular<T>(things, num);
	}

}
