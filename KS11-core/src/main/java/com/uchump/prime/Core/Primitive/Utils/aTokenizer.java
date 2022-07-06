package com.uchump.prime.Core.Primitive.Utils;

import com.uchump.prime.Core.Math.Primitive.aVector;
import com.uchump.prime.Core.Primitive.Struct.aMap;

public class aTokenizer {

	// count & measure symbols as ranges where the number of endcaps per type match
	// scan

	/// Symbol->(from, to , elemental length ,outter depth) , so "[[][]]" would be 3
	/// tokens 1 of (0, 5, 6, 2)
	
	public static String[] encapsulators = new String[] {};
	private aMap<String, aVector> indices = new aMap<String, aVector>();

	public aTokenizer(String input) {

		this.init();
	}

	private void init() {
		this.indices.put("[]", new aVector()); // toValue/Entity, spades
		this.indices.put("<>", new aVector()); // toGroup, diamonds
		this.indices.put("{}", new aVector()); // toSet, hearts
		this.indices.put("()", new aVector()); // toNode, clubs		
	}

	// public aVector generateAddress(Number from, Number to)
	public static class TokenAddress extends aVector {
		// clamps?
		// vec4 address
		public Number from() {
			return this.getDefault(0, Float.NaN);
		}

		public Number to() {
			return this.getDefault(1, Float.NaN);
		}

		public Number bredth() {
			return this.getDefault(2, Float.NaN);
		}

		public Number depth() {
			return this.getDefault(3, Float.NaN);
		}

	}
}
