package com.uchump.prime.Core.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ArrayReflection;
import com.uchump.prime.Core.Math.Primitive.aVector;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Primitive.Struct.aMap;

public class _CSV {
	public static String starts = "({[<";
	public static String ends = ")}]>";

	// CSV array
	public static <T> aList<T> resolveToList(String input) {
		// String starts = "({[<";
		// String ends = ")}]>";
		int first = 0;
		int last = input.length() - 1;

		aList<T> result = new aList<T>();

		StringBuilder process = new StringBuilder(input);
		// Log(input + " <<<<<");
		if (starts.contains("" + input.charAt(first)) && ends.contains("" + input.charAt(last))) {
			process.deleteCharAt(last);
			process.deleteCharAt(first);
			// Log(process + " >>>>>");
		}
		last = process.length() - 1;

		String[] s = process.toString().split(",");
		for (int i = 0; i < s.length; i++) {
			result.append((T) s[i]);
		}

		return result;
	}

	public static String commaLen(int length) {
		String s = "";
		if (length == 1)
			return ",";
		for (int i = 0; i < length - 1; i++)
			s += ",";

		return s;
	}

	public static String resolveVarargs(Object... values) {
		String result = "(";
		for (int i = 0; i < values.length; i++) {
			result += "" + values[i];
			if (i != values.length - 1)
				result += ",";
		}
		result += ")";
		return result;
	}

	public static String resolveVarargs(String encap, Object... values) {
		String result = "" + encap.charAt(0);
		for (int i = 0; i < values.length; i++) {
			result += "" + values[i];
			if (i != values.length - 1)
				result += ",";
		}
		result += encap.charAt(1);
		return result;

	}

	public static <T> aList<T> resolveToList(String input, char delimiter) {
		String starts = "({[<";
		String ends = ")}]>";
		int first = 0;
		int last = input.length() - 1;

		aList<T> result = new aList<T>();

		StringBuilder process = new StringBuilder(input);
		// Log(input + " <<<<<");
		if (starts.contains("" + input.charAt(first)) && ends.contains("" + input.charAt(last))) {
			process.deleteCharAt(last);
			process.deleteCharAt(first);
			// Log(process + " >>>>>");
		}
		last = process.length() - 1;

		String[] s = process.toString().split("" + delimiter);
		for (int i = 0; i < s.length; i++)
			result.append((T) s[i]);

		return result;
	}

	public static <T> aList<T> resolveToList(String input, String delimiter) {
		String starts = "({[<";
		String ends = ")}]>";
		int first = 0;
		int last = input.length() - 1;

		aList<T> result = new aList<T>();

		StringBuilder process = new StringBuilder(input);
		// Log(input + " <<<<<");
		if (starts.contains("" + input.charAt(first)) && ends.contains("" + input.charAt(last))) {
			process.deleteCharAt(last);
			process.deleteCharAt(first);
			// Log(process + " >>>>>");
		}
		last = process.length() - 1;

		String[] s = process.toString().split(delimiter);
		for (int i = 0; i < s.length; i++)
			result.append((T) s[i]);

		return result;
	}

	public static <T> aMap<String, T> resolveToMap(String input, String delimiter) {
		String starts = "({[<";
		String ends = ")}]>";
		int first = 0;
		int last = input.length() - 1;

		String enDex = "";
		// aList<T> result = new aList<T>();
		aMap<String, T> result = new aMap<String, T>();

		StringBuilder process = new StringBuilder(input);
		// Log(input + " <<<<<");
		if (starts.contains("" + input.charAt(first)) && ends.contains("" + input.charAt(last))) {

			enDex = "" + input.charAt(first) + "" + input.charAt(last);
			process.deleteCharAt(last);
			process.deleteCharAt(first);

			// Log(process + " >>>>>");
		}
		last = process.length() - 1;

		String[] s = process.toString().split(delimiter);
		for (int i = 0; i < s.length; i++)
			result.put(enDex, (T) s[i]);

		return result;
	}

	public static <T> Array<T> resolveToArray(String input, Class<T> type) {
		aList<T> toList = resolveToList(input);
		T[] t = (T[]) ArrayReflection.newInstance(type, 0);

		Array<T> result = new Array(t);
		for (int i = 0; i < toList.size(); i++)
			result.add(toList.get(i));
		return result;

	}

	public static Number[] resolveToNumbers(String input) {
		int s = input.length() + 1;

		aList<String> toList = resolveToList(input);
		Number[] trans;
		if (toList.isEmpty()) {
			trans = new Number[s];
			for (int i = 0; i < s; i++)
				trans[i] = 0;
			return trans;
		} else {
			trans = new Number[toList.size()];
			for (int i = 0; i < toList.size(); i++)
				trans[i] = StringUtils.parseNum(toList.get(i));
		}
		return trans;
	}

	public static aVector resolveToVector(String input) {

		aList<String> toList = resolveToList(input);
		Number[] trans = new Number[toList.size()];
		for (int i = 0; i < toList.size(); i++)
			trans[i] = StringUtils.parseNum(toList.get(i));

		aVector result = new aVector(input);
		return result;
	}
	
	
	public static Pattern P_SV(String p)
	{
		String a = "|[^";
		String b = "\"]+|\"(?:[^\"]|\"\")*\"";
		
		return Pattern.compile(p+a+p+b);
	}
	
	 private static final Pattern PATTERN = Pattern.compile(",|[^,\"]+|\"(?:[^\"]|\"\")*\"");
	    public static List<String> parse(final String line, final int max) {
		if (max == 0) {
		    return Arrays.asList(line);
		}
		final List<String> l = new ArrayList<>(max);
		final Matcher m = PATTERN.matcher(line);
		while (l.size() < max && m.find()) {
		    final String v = m.group();
		    if (v.equals(",")) {
			continue;
		    } else if (v.startsWith("\"") && v.endsWith("\"")) {
			l.add(v.substring(1,v.length()-1).replace("\"\"", "\""));
		    } else {
			l.add(v);
		    }
		}
		if (m.find()) {
		    l.add(line.substring(m.end()));
		}
		return l;
	    }
}
