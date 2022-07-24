package com.uchump.prime.Core.Primitive.Utils;

import static com.uchump.prime.Core.uAppUtils.*;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.Struct._Map;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Primitive.Struct.aMultiMap;
import com.uchump.prime.Core.Primitive.Struct.aSet;

public class aThingClassifier {

	public _Map<Class, Object> things;

	public aThingClassifier(Object... things) {
		this(true, things);
	}

	public aThingClassifier(boolean set, Object... things) {
		this.things = new aMultiMap<Class, Object>(new aList<Class>(), new aSet<Object>());
		for (Object o : things)
			this.things.put(o.getClass(), o);
	}

	// merge Int,Float,etc to Number
	public aThingClassifier mergeKeysOf(Class to) {
		aMultiMap<Class, Object> x = (aMultiMap<Class, Object>) this.things.cpy();
		this.things.clear();
		for (_Map.Entry<Class, Object> E : x) {
			if (instanceOf(to).test(E.getValue()))
				this.things.put(to, E.getValue());
			else
				this.things.put(E);

		}
		return this;
	}

	public aThingClassifier mergeKeysOf(Class to, Predicate p) {
		aMultiMap<Class, Object> x = (aMultiMap<Class, Object>) this.things.cpy();
		this.things.clear();
		this.things = new aMultiMap<Class, Object>(new aList<Class>(), new aSet<Object>());
		for (_Map.Entry<Class, Object> E : x) {
			if (p.test(E.getValue()))
				this.things.put(to, E.getValue());
			else
				this.things.put(E);

		}
		return this;
	}

	public aThingClassifier mergeKeys(Class to, Class... c) {
		aMultiMap<Class, Object> x = (aMultiMap<Class, Object>) this.things.cpy();
		this.things.clear();
		for (_Map.Entry<Class, Object> E : x) {
			if (instanceOf(c).test(E.getValue()))
				this.things.put(to, E.getValue());
			else
				this.things.put(E);

		}
		return this;
	}

	public aThingClassifier mergeKeys(Class to, String... c) {
		aMultiMap<Class, Object> x = (aMultiMap<Class, Object>) this.things.cpy();
		this.things.clear();
		for (_Map.Entry<Class, Object> E : x) {
			if (kindOf(c).test(E.getValue()))
				this.things.put(to, E.getValue());
			else
				this.things.put(E);

		}
		return this;
	}

	public aThingClassifier mergeValuesOf(Class c, Function f) {
		aMultiMap<Class, Object> x = (aMultiMap<Class, Object>) this.things.cpy();
		iCollection C = this.things.getAll(c);
		this.things.removeKey(c);
		for (int i = 0; i < C.size() - 1; i++) {
			Object F = f.apply(C.get(i));
			this.things.put(c, F);
		}

		return this;
	}

	// equator
	public aThingClassifier filter(Function... f) {
		this.things.values.filter(f);
		return this;
	}

	public aThingClassifier filterDuplicates() {
		aMultiMap<Class, Object> x = (aMultiMap<Class, Object>) this.things.cpy();
		this.things.clear();
		for (_Map.Entry<Class, Object> E : x) {
			if (!this.things.values.contains(E.getValue()))
				this.things.put(E.getKey(), E.getValue());

		}
		return this;
	}

	public aThingClassifier filter(Predicate... p) {
		aMultiMap<Class, Object> x = (aMultiMap<Class, Object>) this.things.cpy();
		this.things.clear();
		for (_Map.Entry<Class, Object> E : x)
			if (all(p).test(E.getValue()))
				this.things.put(E);

		return this;
	}
	// consolidate values

}
