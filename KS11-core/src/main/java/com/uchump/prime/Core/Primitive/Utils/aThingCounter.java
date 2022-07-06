package com.uchump.prime.Core.Primitive.Utils;

import static com.uchump.prime.Core.uAppUtils.*;
import static com.uchump.prime.Core.uSketcher.*;

import com.uchump.prime.Core.Math.Utils.aMaths;
import com.uchump.prime.Core.Primitive.aNode;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iMap;
import com.uchump.prime.Core.Primitive.Struct._Map.Entry;
import com.uchump.prime.Core.Primitive.Struct.aMap;
import com.uchump.prime.Core.Primitive.Struct.aSet;
import com.uchump.prime.Core.Primitive.Struct.aSetMap;

public class aThingCounter<T> {

	public aMap<T, aNode<Integer>> things;
	int sum = 0;

	public aThingCounter(T... things) {
		this.sum = things.length;
		this.things = new aMap<T, aNode<Integer>>();
		for (int i = 0; i < things.length; i++) {
			if (!this.things.containsKey(things[i]))
				this.things.put(things[i], new aNode<Integer>(1));
			else {
				int v = this.things.get(things[i]).get();
				this.things.get(things[i]).set(v + 1);
			}
		}
	}

	public aThingCounter(iCollection<T> things) {
		this.things = new aMap<T, aNode<Integer>>();
		for (int i = 0; i < things.size(); i++) {
			if (!this.things.containsKey(things.get(i)))
				this.things.put((T) things.get(i), new aNode<Integer>(1));
			else {
				int v = this.things.get(things.get(i)).get();
				this.things.get(things.get(i)).set(v + 1);
			}
		}

	}

	public aThingCounter(iMap things) {
		this(things.getEntries());
		// remaps the map into nodes of individual entry
		// effectively emulating aSetMap
		// ie aMap[3x4]
		// 0=>
		// | 0->0 ASS|{42,66}
		// | 1->1 BUTTS|{7}
		// | 2->2 CACTUS|{p}
		// vvv
		// aMap[4x4
		// o=>
		// | ""
		// | 0->0 ASS|42
		// | 1->1 ASS|66
		// | 2->2 BUTTS|7
		// | 3->3 CACTUS|9

	}

	public aThingCounter count() {
		return this;
	}

	public static aThingCounter countThings(Object... things) {
		return new aThingCounter(things);
	}

	public int getCountOf(T o) {
		if (this.things == null || !this.things.containsKey(o))
			return -1;
		return this.things.get(o).get();
	}

	public T getMostFreq() {
		Entry<T, aNode<Integer>> lastMax = null;
		for (Entry<T, aNode<Integer>> E : this.things) {
			if (lastMax == null)
				lastMax = E;

			else if (E.getValue().get() > lastMax.getValue().get())
				lastMax = E;

		}
		return lastMax.getKey();
	}

	public int sum() {
		int s = 0;
		for (int i = 0; i < this.things.getEntries().size(); i++) {
			Entry<T, aNode<Integer>> E = this.things.getEntries().get(i);
			s += E.getValue().get();

		}
		this.sum = s;
		return s;
	}

	// frequency & banding
	public float frequencyOf(T thing) {
		// Log(this.things.toLog());
		// Log(this.things.containsValue(thing) + "<=" + thing);
		if (this.things == null || this.things.isEmpty() || !this.things.keys.contains(thing))
			return 0f;
		if (this.things.size() == 1)
			return 1f;

		int S = this.sum();
		int s = this.things.size() / S;
		// Log(s+"~>"+S);
		float F = aMaths.inverseLerp(s, S, (float) this.getCountOf(thing)).floatValue();

		return F;
	}

	public void dispose() {
		this.things.clear();
		this.things = null;
	}

	public aSetMap<T, Float> FreqMap() {
		aSetMap<T, Float> res = new aSetMap<T, Float>();

		for (T t : this.things.keys)
			res.put(t, this.frequencyOf(t));

		return res;
	}
	
	@Override
	public String toString() {
		String s = "";
		for (Entry E : this.things) {
			s += "[" + E.getKey() + " | " + ((aNode) E.getValue()).value + "]\n";
		}
		return s;
	}

	public String toLog() {
		String log = this.getClass().getSimpleName() + "{" + this.things.keys.size() + "}\n";
		log += this.things.keys.toSet().size() + "x" + this.things.values.size() + "\n";
		/*
		 * for (int i = 0; i < this.keys.size(); i++) { log += "[" + i + "]" +
		 * this.keys.get(i) + "|" + this.values.get(i) + "\n"; }
		 */
		aSet<String> L = new aSet<String>();
		aSet<T> thisKeys = this.things.keys.toSet();
		for (T k : thisKeys) {
			int i = thisKeys.indexOf(k);
			// for(int i =0; i < this.keys.size(); i++) {
			// K k = keys.get(i);
			L.append("->[" + i + "] " + k + " |" + this.things.get(k).value);
		}
		for (int i = 0; i < L.size(); i++)
			log += "[" + i + "]" + L.get(i) + "\n";

		return log;
	}
	

}
