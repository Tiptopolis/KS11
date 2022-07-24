package com.uchump.prime.Core.Primitive.Struct.Impl;

import com.uchump.prime.Core.Primitive.Struct._Map;
import com.uchump.prime.Core.Utils.StringUtils;

public class aHyperkey extends _Map.Entry<_Map.Entry<Object, String>, Object> {

	public boolean is(String label) {
		_Map.Entry<Object, String> E = this.getKey();

		boolean eK = false;
		boolean eV = false;
		if (StringUtils.isFormOf("" + E.getKey(), label))
			eK = true;
		if (StringUtils.isFormOf("" + E.getValue(), label))
			eV = true;

		return eK || eV;
		
	}

	public static boolean is(_Map.Entry E, String label) {

		if (E.getKey() instanceof _Map.Entry) {
			_Map.Entry M = (_Map.Entry) E.getKey();
			String S = "" + M.getValue();
			// Log("** " + S + " " + label);
			if (StringUtils.isFormOf(S, label) || StringUtils.containsWord(S, label))
				return true;
		}

		return false;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof _Map.Entry) {
			_Map.Entry M = (_Map.Entry) other;
			boolean K = this.getKey() == M.getKey() || this.getKey().equals(M.getKey());
			boolean V = this.getValue() == M.getValue() || this.getValue().equals(M.getValue());
			return K && V;
		}
		return false;
	}

}
