package com.uchump.prime.Metatron.Util;

import com.uchump.prime.Core.DefaultResources;
import com.uchump.prime.Core.Primitive.aValue;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Primitive.Struct.aMultiMap;
import com.uchump.prime.Core.Primitive.Struct.aMap;
import com.uchump.prime.Core.Utils.iCypher;

public abstract class _FN {

	public static aMultiMap<String, aValue> paramSig(aValue... values) {
		aMultiMap<String, aValue> rtn = new aMultiMap<String, aValue>();
		for (aValue v : values) {
			rtn.put(v.label(), v);
		}
		return rtn;
	}

	public static aMultiMap<String, aValue> paramSig(Object... values) {

		aList<aValue> vals = new aList<aValue>();

		for (int i = 0; i < values.length; i++) {
			if (!(values[i] instanceof aValue))
				vals.append(new aValue("" + i, values[i]));
			else
				vals.append((aValue) values[i]);
		}

		aMultiMap<String, aValue> rtn = new aMultiMap<String, aValue>();
		for (aValue v : vals) {
			rtn.put(v.label(), v);
		}
		return rtn;
	}

	public static aMultiMap<String, aValue> inputSig(Object... values) {
		int rdx = DefaultResources.ENGLISH_LETTERS_UPPER.length();

		aList<aValue> vals = new aList<aValue>();
		for (int i = 0; i < values.length; i++) {
			char c = ' ';

			if (!(values[i] instanceof aValue)) {
				if (values.length == 1)
					vals.append(new aValue("IN", values[i]));
				else
					vals.append(new aValue("IN." + iCypher.rdxComp(i), values[i]));
			} else
				vals.append((aValue) values[i]);
		}

		aMultiMap<String, aValue> rtn = new aMultiMap<String, aValue>();
		for (aValue v : vals) {
			rtn.put(v.label(), v);
		}
		return rtn;
	}

	public static aMultiMap<String, aValue> outputSig(Object... values) {
		int rdx = DefaultResources.ENGLISH_LETTERS_UPPER.length();

		aList<aValue> vals = new aList<aValue>();
		for (int i = 0; i < values.length; i++) {
			char c = ' ';

			if (!(values[i] instanceof aValue)) {
				if (values.length == 1)
					vals.append(new aValue("OUT", values[i]));
				else
					vals.append(new aValue("OUT." + iCypher.rdxComp(i), values[i]));
			} else
				vals.append((aValue) values[i]);
		}

		aMultiMap<String, aValue> rtn = new aMultiMap<String, aValue>();
		for (aValue v : vals) {
			rtn.put(v.label(), v);
		}
		return rtn;
	}

	public static aMultiMap<String, aValue> variableSig(Object... values) {
		return varSig("VAR", DefaultResources.ENGLISH_LETTERS_LOWER, values);
	}

	public static aMultiMap<String, aValue> varSig(String vName, String codex, Object... values) {
		aList<aValue> vals = new aList<aValue>();
		for (int i = 0; i < values.length; i++) {
			char c = ' ';

			if (!(values[i] instanceof aValue))
				vals.append(new aValue(vName + "." + iCypher.rdxComp(codex, i), values[i]));
			else
				vals.append((aValue) values[i]);
		}

		aMultiMap<String, aValue> rtn = new aMultiMap<String, aValue>();
		for (aValue v : vals) {
			rtn.put(v.label(), v);
		}
		return rtn;
	}

	public static aMap<String, String> sigAlias(String... keys) {
		// so: KeySymbol[1] -> In.A

		aMultiMap<String, aValue> inp = inputSig(keys);
		aMap<String, String> res = new aMap<String, String>();

		for (int i = 0; i < keys.length; i++) {
			res.put(keys[i], inp.keys.get(i));
		}

		inp.clear();
		return res;
	}

	public static boolean sigEquals(Object[] a, Object[] b) {

		boolean out = true;

		aMultiMap<String, aValue> A = inputSig(a);
		aMultiMap<String, aValue> B = inputSig(b);
		if (A.getEntries().size() != B.getEntries().size())
			return false;

		int s = java.lang.Math.min(a.length, b.length);
		for (int i = 0; i < s; i++) {
			aValue vA = A.getEntries().get(i).getValue();
			aValue vB = B.getEntries().get(i).getValue();
			// Log(">>" + vA.get.getClass());
			if (!(vA.get().getClass().getSimpleName().equals(vB.get().getClass().getSimpleName())))
				return false;
		}

		return out;
	}
	
}
