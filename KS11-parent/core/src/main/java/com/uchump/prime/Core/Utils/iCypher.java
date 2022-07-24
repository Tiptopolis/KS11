package com.uchump.prime.Core.Utils;

import static com.uchump.prime.Core.uAppUtils.*;

import com.uchump.prime.Core.DefaultResources;
import com.uchump.prime.Core.Math.N_Operator;
import com.uchump.prime.Core.Math.Primitive.aVector;
import com.uchump.prime.Core.Math.Utils.Maths;
import com.uchump.prime.Core.Math.Utils.aMaths;
import com.uchump.prime.Core.Primitive.aValue;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Primitive.Struct.aMultiMap;
import com.uchump.prime.Core.Primitive.Struct.aSet;
import com.uchump.prime.Core.Primitive.Struct.aMap;
import com.uchump.prime.Core.Primitive.Utils.aThingCounter;

public abstract class iCypher {
	// translate theta->delta
	public static String starts = "({[< ";
	public static String ends = ")}]> ";
	public static String delimiters = ",_./\\:; ";
	public static String[] encapsulators = new String[] { "()", "{}", "[]", "<>" };
	public static String terminators = ";:.| ";
	public static String modifiers = "@#$?~`";
	public static String[] operators = new String[] { "+-*/%", "&|!^" };

	static final char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	static final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	static String alphabetA = "abcdefghijklmnopqrstuvwxyz";
	static String alphabetB = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static String alphabetC = "-_+-*/!,.?@#$%^&*(){}[]<>/\';:`~";
	static String alphabetD = "1234567890";
	static String alphabetE = "0123456789";
	static String alphabetF = ",.+-*/!?#$%^&*;:";
	static String alphabetG = "1234567890ABCDEF";
	static String alphabetH = "0123456789ABCDEF";
	static String alphabetI = alphabetE + alphabetB;

	// generates an alphabet string
	public static String NumericBaseAlphabet(int base) {
		if (base > 0 && base <= 10)
			return alphabetE.substring(0, base);
		else if (base < (alphabetE.length() + alphabetB.length())) {
			int diff = base - alphabetE.length();
			return "" + (alphabetI).substring(0, base);
		} else {
			int diff = base - alphabetE.length();
			int l = (alphabetI).length();
			String s = rdxComp(alphabetI.substring(0, l % base), base);
			return s;
		}

	}

	// nor?
	public static String decodeVector(aVector form, String alphabet) {
		// "aaaa" = (1,1,1,1)

		int raddix = alphabet.length();

		String res = "";

		for (int i = 0; i < form.size(); i++) {

			Number index = resolveModIndex(form.get(i).intValue(), raddix);
			res += "" + alphabet.charAt(index.intValue());

		}

		return res;
	}

	public static aVector<Integer> encodeVector(String source) {
		int l = source.length();
		String s = source.toLowerCase();
		Integer[] n = new Integer[l];

		for (int i = 0; i < l; i++) {
			//n[i] = (int) source.charAt(i);		
			n[i] = DefaultResources.ENGLISH_LETTERS_LOWER.indexOf(source.charAt(i));
		}

		return new aVector(n);
	}

	public static aVector<Integer> encodeVector(int[] values, String alphabet) {

		int l = values.length;
		int r = alphabet.length();
		String s = "";
		Integer[] n = new Integer[l];
		for (int i = 0; i < l; i++) {
			int I = resolveModIndex(values[i], r).intValue();
			n[i] = I;
		}

		return new aVector(n);
	}



	// __________i_s_______________________
	// InnerPos @2#10-> @%# = 2
	// OutterPos @12#10->2 @%# = 2
	// InnerNeg @-2#10-> abs(#+@)%# = 8
	// OutterNeg @-12#10-> abs(#+@)%# = 8

	public static Number resolveModIndex(Number n, Number raddix) {
		return resolveModIndex(n, N_Operator.resolveTo(0, raddix), raddix);
	}

	public static Number resolveModIndex(Number n, Number min, Number max) {
		Number IndexT = N_Operator.resolveTo(n, min);
		Number Z = N_Operator.resolveTo(min, IndexT);

		if (N_Operator.isEqual(IndexT, Z))
			return Z;

		Number il = aMaths.inverseLerp(min, max, n);
		Number ip = il.floatValue() % 1f;

		Number I = aMaths.lerp(min, max, ip).intValue();

		if (!aMaths.isPositive(I))
			I = N_Operator.add(max, I);
		return N_Operator.resolveTo(I, IndexT);
	}

	public static String rdxComp(int val) {

		return rdxComp(DefaultResources.ENGLISH_LETTERS_UPPER, val);
	}

	public static String rdxComp(String alphabet, int val) {
		String out = "";
		int rdx = alphabet.length();

		Number fA = N_Operator.quo(val, rdx);
		Number fB = N_Operator.mod(val, rdx);

		for (int i = 0; i < fA.intValue(); i++)
			out += "" + alphabet.charAt(i);

		out += "" + alphabet.charAt(fB.intValue());

		return out;
	}

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

			if (!(values[i] instanceof aValue))
				vals.append(new aValue("IN." + iCypher.rdxComp(i), values[i]));
			else
				vals.append((aValue) values[i]);
		}

		aMultiMap<String, aValue> rtn = new aMultiMap<String, aValue>();
		for (aValue v : vals) {
			rtn.put(v.label(), v);
		}
		return rtn;
	}

	public static aVector[] fillPermutations(aVector dimensions) {
		return listCoords(dimensions);
	}

	public aVector[] fillPermutations(Number... dimensions) {
		return listCoords(new aVector(dimensions));
	}

	public static aVector[] listCoords(aVector dimensions) {
		aVector cumulatives = new aVector(dimensions.value).resize(dimensions.size());
		int total = 1;
		for (int d = dimensions.size() - 1; d >= 0; d--) {
			cumulatives.setAt(d, total);
			total *= dimensions.get(d).intValue();
		}

		aVector[] coords = new aVector[total];
		for (int i = 0; i < total; i++) {
			aVector coord = new aVector(dimensions.value).resize(dimensions.size());
			for (int d = dimensions.size() - 1; d >= 0; d--) {
				coord.setAt(d, (int) Math.floor(i / cumulatives.get(d).intValue()) % dimensions.get(d).intValue());
			}
			coords[i] = coord;
		}
		return coords;
	}

	/**
	 * List permutations of a string.
	 * 
	 * @param s the input string
	 * @return the list of permutations
	 */
	public static iCollection<String> permutation(String s) {
		aList<String> res = new aList<String>();
		if (s.length() == 1) {
			res.add(s);
		} else if (s.length() > 1) {
			int lastIndex = s.length() - 1;
			// Find out the last character
			String last = s.substring(lastIndex);
			// Rest of the string
			String rest = s.substring(0, lastIndex);
			// Perform permutation on the rest string and
			// merge with the last character
			res = (aList<String>) merge(permutation(rest), last);
		}
		return res;
	}

	/**
	 * @param list a result of permutation, e.g. {"ab", "ba"}
	 * @param c    the last character
	 * @return a merged new list, e.g. {"cab", "acb" ... }
	 */
	public static iCollection<String> merge(iCollection<String> list, String c) {
		aList<String> res = new aList<String>();
		// Loop through all the string in the list
		for (String s : list) {
			// For each string, insert the last character to all possible positions
			// and add them to the new list
			for (int i = 0; i <= s.length(); ++i) {
				String ps = new StringBuffer(s).insert(i, c).toString();
				res.add(ps);
			}
		}
		return res;
	}

	public static aMap<String, String> staticLinkMap(String... keys) {
		// so: KeySymbol[1] -> In.A

		aMultiMap<String, aValue> inp = inputSig(keys);
		aMap<String, String> res = new aMap<String, String>();

		for (int i = 0; i < keys.length; i++) {
			res.put(keys[i], inp.keys.get(i));
		}

		inp.clear();
		return res;
	}

	public static int extMag(String input) {
		String delimiters = ":_";
		int c = 0;
		for (int i = 0; i < input.length(); i++)
			if (delimiters.contains("" + input.charAt(i)))
				c++;

		return c;
	}

	// Caeser Cipher
	public static String cypher(String input, int offset) {
		String s = "";
		String I = input;

		int alphaRdx = alphabetA.length();
		for (int i = 0; i < I.length(); i++) {

			if (alphabetC.contains("" + I.charAt(i))) {
				s += "" + I.charAt(i);
			}
			if (alphabetD.contains("" + I.charAt(i))) {
				s += "" + I.charAt(i);
			}

			else if (alphabetA.contains("" + I.charAt(i))) {
				int map = alphabetA.indexOf(I.charAt(i));
				int mapTo = (map + offset) % alphaRdx;

				s += alphabetA.charAt(mapTo) + "";
			} else if (alphabetB.contains("" + I.charAt(i))) {
				int map = alphabetB.indexOf(I.charAt(i));
				int mapTo = (map + offset) % alphaRdx;

				s += alphabetB.charAt(mapTo) + "";
			}
		}

		return s;
	}

	public static String decypher(String input, int offset) {
		String s = "";
		String I = input;

		int alphaRdx = alphabetA.length();
		for (int i = 0; i < I.length(); i++) {

			if (alphabetC.contains("" + I.charAt(i))) {
				s += "" + I.charAt(i);
			}
			if (alphabetD.contains("" + I.charAt(i))) {
				s += "" + I.charAt(i);
			}

			else if (alphabetA.contains("" + I.charAt(i))) {
				int map = alphabetA.indexOf(I.charAt(i));
				int mapTo = (map - offset) % alphaRdx;

				s += alphabetA.charAt(mapTo) + "";
			} else if (alphabetB.contains("" + I.charAt(i))) {
				int map = alphabetB.indexOf(I.charAt(i));
				int mapTo = (map - offset) % alphaRdx;

				s += alphabetB.charAt(mapTo) + "";
			}
		}

		return s;
	}

	public static aList<String> decompose(String s) {
		aSet<String> d = new aSet<String>();
		aSet<String> e = new aSet<String>();
		for (int i = 0; i < delimiters.length(); i++)
			d.append("" + delimiters.charAt(i));
		for (String S : encapsulators)
			e.append(S);

		return decompose(s, d, e);
	}

	public static aList<String> decompose(String s, aSet<String> delimiters, aSet<String> encapsulators) {
		aList<String> res = new aList<String>();

		// int fromIndex -> toIndex
		// no unaccounted sub-strings between F&T //false,true lol

		for (int i = 0; i < s.length(); i++) {
			if (StringUtils.contains(encapsulators, "" + s.charAt(i))) {
				aList<String> token = decompEndcap(i, s.substring(i), encapsulators);
				Log("***********");
				Log(token);
			}
		}

		return res;
	}

	private static aList<String> decompEndcap(int hitIndex, String s, aSet<String> encapsulators) {
		aThingCounter check = new aThingCounter(s.toCharArray());

		aList<String> res = new aList<String>();

		return res;
	}

	public static aMultiMap<String, String> decompMap1(String s) {
		// myString.split("[\\(||\\)]");

		aMultiMap<String, String> res = new aMultiMap<String, String>();
		String[] symbols = new String[] { "T:", "[]", "{}", "()", "<>", "C:", " " };

		// String sqr = "[]";
		// String cur = "{}";
		// String par = "()";
		// String ang = "<>";
		// String[] a = s.split("[\\[||\\]]");
		// String[] b = s.split("[\\{||\\}]");
		// String[] c = s.split("[\\(||\\)]");
		// String[] d = s.split("[\\<||\\>]");
		String[] e = s.split("[\\ ||\\ ]"); // whitespace

		// i%symbols.length

		for (String S : e) {
			if (!S.isBlank())
				res.put(symbols[0], S);

			if (!S.isBlank()) {
				if (!StringUtils.containsAnyOf(S, "{}", "()", "<>"))
					res.put(symbols[1], S);
				if (!StringUtils.containsAnyOf(S, "[]", "()", "<>"))
					res.put(symbols[2], S);
				if (!StringUtils.containsAnyOf(S, "[]", "{}", "<>"))
					res.put(symbols[3], S);
				if (!StringUtils.containsAnyOf(S, "[]", "()", "{}"))
					res.put(symbols[4], S);

			}

		}

		return res;

	}

	//eval @ gometric factors of 2 (2x2x2x2...)
	public static String[] PascalTri(int n)
	{
		String[] res = new String[n+1];
		int  i, j;
	     
	      for(i = 0; i <= n; i++) {
	    	  String nl = "";
	         for(j = 0; j <= n-i; j++){
	            nl+=(" ");
	         }
	         for(j = 0; j <= i; j++){
	        	 nl+=(" "+aMaths.ncr(i, j));
	         }
	         res[i]=nl;
	      }
	      return res;
	}
	
}
