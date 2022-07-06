package com.uchump.prime.Core.Utils;

import static com.uchump.prime.Core.uAppUtils.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ArrayReflection;
import com.uchump.prime.Core.DefaultResources;
import com.uchump.prime.Core.Math.N_Operator;
import com.uchump.prime.Core.Math.Primitive.aVector;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iGroup;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Primitive.Struct.aMap;
import com.uchump.prime.Core.Primitive.Struct._Array;
import com.uchump.prime.Core.Primitive.Struct._Map.Entry;
import com.uchump.prime.Core.Primitive.Struct.aSet;
import com.uchump.prime.Core.Primitive.Struct.aSetMap;
import com.uchump.prime.Core.Primitive.Utils.aThingCounter;

public class StringUtils {
	public static String starts = "({[<";
	public static String ends = ")}]>";
	public static final String emailRegexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
			+ "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

	// [(N) <OP> (N)] -> {(N),<OP>,(N)}
	// node patterns

	public static String substring(String source, int beginIndex, int endIndex) {
		if (source == null || source.isEmpty())
			return "";
		if (beginIndex < 0)
			beginIndex = 0;
		if (endIndex < 0 || endIndex > source.length())
			endIndex = source.length();
		if (beginIndex > endIndex)
			return "";
		return source.substring(beginIndex, endIndex);
	}

	// need a WhyEquals in Node & Type
	public static boolean isFormOf(String A, String B) {

		if (A.toLowerCase().equals(B.toLowerCase()))
			return true;
		else
			return false;
	}

	public static int count(final String source, final String search) {
		if (source == null || search == null || source.isEmpty() || search.isEmpty())
			return 0;
		int amount = 0, idx = -1;
		while ((idx = source.indexOf(search, idx + 1)) >= 0)
			++amount;
		return amount;
	}

	public static int count(final String source, final int search) {
		if (source == null || source.isEmpty())
			return 0;
		int amount = 0, idx = -1;
		while ((idx = source.indexOf(search, idx + 1)) >= 0)
			++amount;
		return amount;
	}

	public static int count(final String source, final String search, final int startIndex, int endIndex) {
		if (endIndex < 0)
			endIndex = 0x7fffffff;
		if (source == null || search == null || source.isEmpty() || search.isEmpty() || startIndex < 0
				|| startIndex >= endIndex)
			return 0;
		int amount = 0, idx = startIndex - 1;
		while ((idx = source.indexOf(search, idx + 1)) >= 0 && idx < endIndex)
			++amount;
		return amount;
	}

	public static int count(final String source, final int search, final int startIndex, int endIndex) {
		if (endIndex < 0)
			endIndex = 0x7fffffff;
		if (source == null || source.isEmpty() || startIndex < 0 || startIndex >= endIndex)
			return 0;
		int amount = 0, idx = startIndex - 1;
		while ((idx = source.indexOf(search, idx + 1)) >= 0 && idx < endIndex)
			++amount;
		return amount;
	}

	public static aMap<String, Integer> countSymbols(String s) {
		aMap<String, Integer> res = GeneralUtils.Counter(CharArray(s));
		return res;
	}

	public static aMap<String, Integer> countSymbolsFrom(String s, String from) {

		aList<String> R1 = toList(CharArray(s));
		aList<String> R2 = (aList<String>) R1.intersection(toList(CharArray(from)));

		return new aThingCounter(R2).things;
	}

	public static String[] CharArray(String s) {
		String[] res = new String[s.length()];
		for (int i = 0; i < s.length(); i++)
			res[i] = "" + s.charAt(i);
		return res;

	}

	public static aList<String> toList(String... s) {
		return new aList<String>(s);
	}

	public static String join(String...to)
	{
		String r = "";
		for(int i =0; i < to.length; i++)
			r+=to[i];
		return r;
	}

	public static String[] split(String source, String delimiter) {
		int amount = count(source, delimiter);
		if (amount <= 0)
			return new String[] { source };
		String[] splat = new String[amount + 1];
		int dl = delimiter.length(), idx = -dl, idx2;
		for (int i = 0; i < amount; i++) {
			splat[i] = substring(source, idx + dl, idx = source.indexOf(delimiter, idx + dl));
		}
		if ((idx2 = source.indexOf(delimiter, idx + dl)) < 0) {
			splat[amount] = substring(source, idx + dl, source.length());
		} else {
			splat[amount] = substring(source, idx + dl, idx2);
		}
		return splat;
	}

	public static Predicate<String> sameWord(String a) {
		return o -> o.toUpperCase().equals(a.toUpperCase());
	}

	public static Predicate<String> contains(String a) {
		return o -> o.contains(a);
	}

	public static Predicate<String> containsWord(String a) {
		return o -> o.toUpperCase().contains(a.toUpperCase());
	}

	public static boolean containsWord(String A, String B) {

		if (A.contains(B) || A.toUpperCase().contains(B.toUpperCase()) || A.toLowerCase().contains(B.toLowerCase()))
			return true;

		return false;
	}

	public static Predicate<String> someFormOf(String A, String... B) {

		return new Predicate<String>() {

			@Override
			public boolean test(String t) {
				for (String s : B)
					if (isFormOf(s, t))
						return false;
				return true;
			}

		};
	}

	public static boolean contains(CharSequence text, char[] search) {
		return !(text == null || text.length() == 0 || search == null || search.length <= 0)
				&& containsPart(text, search, "", "") == search.length;
	}

	public static boolean contains(iGroup strings, String substring) {
		for (Object s : strings) {
			if (s instanceof String) {
				String S = "" + s;
				if (S.contains(substring))
					return true;
			}
		}
		return false;
	}

	public static boolean containsAny(String subject, String... check) {
		for (String s : check)
			if (subject.contains(s))
				return true;

		return false;
	}

	public static boolean containsAnyOf(String subject, String... check) {
		aSet<String> Check = new aSet<String>();

		for (String s : check) {
			for (int i = 0; i < s.length(); i++) {
				String S = "" + s.charAt(i);
				Check.append(S);
			}
		}

		String[] C = new String[Check.size()];
		for (int i = 0; i < Check.size(); i++)
			C[i] = Check.get(i);

		return containsAny(subject, C);
	}

	public static boolean containsNumOf(int Num, String subject, String... of) {
		int c = 0;
		for (String s : of)
			if (subject.contains(s))
				c++;

		if (c == Num)
			return true;
		else
			return false;
	}

	public static boolean containsAnyWord(String subject, String... check) {
		for (String s : check)
			if (containsWord(subject, s))
				return true;

		return false;
	}

	public static boolean containsWord(iGroup strings, String substring) {
		for (Object s : strings) {
			if (s instanceof String) {
				String S = "" + s;
				if (S.toUpperCase().contains(substring.toUpperCase()))
					return true;
			}
		}
		return false;
	}

	public static int containsPart(CharSequence text, char[] search) {
		return containsPart(text, search, "", "");
	}

	public static int containsPart(CharSequence text, char[] search, CharSequence prefix, CharSequence suffix) {
		if (prefix == null)
			prefix = "";
		if (suffix == null)
			suffix = "";
		int bl = prefix.length(), el = suffix.length();
		if (text == null || text.length() == 0 || search == null || (search.length + bl + el <= 0))
			return 0;
		int sl = bl + search.length + el, tl = text.length() - sl, f = 0, sl2 = sl - el;
		char s = (bl <= 0) ? (search.length <= 0 ? suffix.charAt(0) : search[0]) : prefix.charAt(0);
		PRIMARY: for (int i = 0; i <= tl; i++) {
			if (text.charAt(i) == s) {
				for (int j = i + 1, x = 1; x < sl; j++, x++) {
					if (x < bl) {
						if (text.charAt(j) != prefix.charAt(x)) {
							f = Math.max(f, x);
							continue PRIMARY;
						}
					} else if (x < sl2) {
						if (text.charAt(j) != search[x - bl]) {
							f = Math.max(f, x);
							continue PRIMARY;
						}
					} else {
						if (text.charAt(j) != suffix.charAt(x - sl2)) {
							f = Math.max(f, x);
							continue PRIMARY;
						}
					}
				}
				return sl;
			}
		}
		return f;
	}

	public static String commaLen(int length) {
		String s = "";
		if (length == 1)
			return ",";
		for (int i = 0; i < length - 1; i++)
			s += ",";

		return s;
	}

	public static String binaryString(boolean... elements) {
		if (elements == null)
			return "N";
		if (elements.length == 0)
			return "";
		StringBuilder sb = new StringBuilder(64);
		for (int i = 0; i < elements.length; i++) {
			sb.append(elements[i] ? '1' : '0');
		}
		return sb.toString();
	}

	public static String rdxComp(int val) {
		// A->Z:AA->ZZ:AAA->ZZZ
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

	private static void METATRON_TYPE_STUFF_______() {

	}

	public static boolean validEntityName(String s) {
		if (s.charAt(0) == '[' && s.charAt(s.length() - 1) == ']')
			return true;
		else
			return false;
	}

	public static boolean validTypeName(String s) {
		if (s.charAt(0) == '<' && s.charAt(s.length() - 1) == '>')
			return true;
		else
			return false;
	}

	public static boolean validParameterEntry(String s) {
		if (s.charAt(0) == '(' && s.charAt(s.length() - 1) == ')')
			return true;
		else
			return false;
	}

	public static boolean validListName(String s) {
		if (s.charAt(0) == '{' && s.charAt(s.length() - 1) == '}')
			return true;
		else
			return false;
	}

	public static char[] toCharArray(String s) {
		char[] c = new char[s.length()];
		for (int i = 0; i < s.length(); i++) {
			c[i] = s.charAt(i);
		}
		return c;
	}

	public static String[] toArray(String s) {
		String[] c = new String[s.length()];
		for (int i = 0; i < s.length(); i++) {
			c[i] = "" + s.charAt(i);
		}
		return c;
	}

	public static String toName(String str) {
		String s = str.substring(0, 1).toUpperCase();
		String tr = str.substring(1).toLowerCase();
		return s + tr;
	}

	public static String toMoney(float amt) {
		Locale locale = new Locale("en", "US");
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
		return (currencyFormatter.format(amt));
	}

	public static char numberFormat(Number n) {

		if (n instanceof aVector)
			return numberFormat(((aVector) n).value);

		if (n instanceof Integer)
			return 'i';
		if (n instanceof Float)
			return 'f';
		if (n instanceof Double)
			return 'd';
		if (n instanceof Short)
			return 's';
		if (n instanceof Long)
			return 'l';
		if (n instanceof Byte)
			return 'b';
		if (n instanceof BigDecimal)
			return 'B';

		return 'n';

	}

	public static String add(String a, String b) {
		String c = "";
		return c + (a + b);
	}

	public static String sub(String a, String b) {
		String c = "";
		int s = Math.min(a.length(), b.length());
		StringBuilder A = new StringBuilder(a);

		for (int i = 0; i < a.length(); i++)
			for (int j = 0; j < b.length(); j++)
				if (a.charAt(i) == b.charAt(j))
					A.deleteCharAt(i);

		c = A.toString();
		return c;
	}

	private static void ________METATRON_TYPE_STUFF() {

	}

	public static aMap<String, Integer> charIndexMap(String thing) {
		// map characters to specific indices of the given String

		aMap<String, Integer> res = new aMap<String, Integer>();

		for (int i = 0; i < thing.length(); i++)
			res.put("" + thing.charAt(i), i);

		return res;
	}

	public static boolean validAmount(String amount) {
		String alph = DefaultResources.ENGLISH_LETTERS;
		for (int i = 0; i < alph.length(); i++) {
			if (amount.contains(alph.substring(i, i + 1)))
				return false;
		}
		return true;
	}

	public static boolean validFloat(String input) {
		if (validAmount(input))
			if (input.contains(".") && !input.contains(","))
				return true;

		return false;
	}

	public static boolean validInt(String input) {
		if (validAmount(input))
			if (!input.contains(".") && !input.contains(","))
				return true;

		return false;
	}

	public static int parseInt(String input) {
		if (validInt(input) && !validFloat(input))
			return Integer.parseInt(input);
		else if (validFloat(input) && validInt(input))
			return (int) Float.parseFloat(input);
		return -1;
	}

	public static float parseFloat(String input) {
		if (validFloat(input) || validInt(input))
			return Float.parseFloat(input);
		else
			return Float.NaN;
	}

	public static Number parseNum(String input) {
		return N_Operator.resolve(input);
	}

	public static int decLength(float f) {
		String F = "" + f;
		int i = F.lastIndexOf(".");
		return F.length() - i;
	}

	public static boolean startsWith(String a, String b) {
		return a.toLowerCase().startsWith(b.toLowerCase());
	}

	public static boolean endsWith(String a, String b) {
		return a.toLowerCase().endsWith(b.toLowerCase());
	}

	public static boolean validEmail(String emailAddress) {
		return patternMatches(emailAddress, emailRegexPattern);
	}

	public static boolean patternMatches(String input, String regexPattern) {
		return Pattern.compile(regexPattern).matcher(input).matches();
	}

	// public static void main(String... args) {
	// String a = "25.01";
	// Log(a + " " + validAmount(a));
	// }

	public static String buildArgsString(int length) {
		String args = "";

		args += "(";
		for (int i = 0; i < length; i++) {
			args += "?";
			if (i != length - 1)
				args += ",";
		}
		args += ")";

		return args;
	}

	// CSV array
	public static <T> aList<T> resolveCsvToList(String input) {
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

	public static String resolveVarargsToCSV(Object... values) {
		String result = "(";
		for (int i = 0; i < values.length; i++) {
			result += "" + values[i];
			if (i != values.length - 1)
				result += ",";
		}
		result += ")";
		return result;
	}

	public static String resolveVarargsToCSV(String encap, Object... values) {
		String result = "" + encap.charAt(0);
		for (int i = 0; i < values.length; i++) {
			result += "" + values[i];
			if (i != values.length - 1)
				result += ",";
		}
		result += encap.charAt(1);
		return result;

	}

	public static aList<String> resolveCsvToList(String input, char delimiter) {
		String starts = "({[<";
		String ends = ")}]>";
		int first = 0;
		int last = input.length() - 1;

		aList<String> result = new aList<String>();

		StringBuilder process = new StringBuilder(input);
		// Log(input + " <<<<<");
		if (starts.contains("" + input.charAt(first)) && ends.contains("" + input.charAt(last))) {
			process.deleteCharAt(last);
			process.deleteCharAt(first);
			// Log(process + " >>>>>");
		}
		last = process.length() - 1;

		String[] s = process.toString().split("\\" + delimiter);

		for (int i = 0; i < s.length; i++)
			result.append(s[i]);

		return result;
	}

	public static aList<String> resolveCsvToList(String input, String delimiter) {
		String starts = "({[<";
		String ends = ")}]>";
		int first = 0;
		int last = input.length() - 1;

		aList<String> result = new aList<String>();

		StringBuilder process = new StringBuilder(input);
		// Log(input + " <<<<<");
		if (starts.contains("" + input.charAt(first)) && ends.contains("" + input.charAt(last))) {
			process.deleteCharAt(last);
			process.deleteCharAt(first);
			// Log(process + " >>>>>");
		}
		last = process.length() - 1;

		String[] s = process.toString().split("\\" + delimiter);

		for (int i = 0; i < s.length; i++)
			result.append(s[i]);

		return result;
	}

	public static <T> aMap<String, T> resolveCsvToMap(String input, String delimiter) {
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

	public static <T> Array<T> resolveCsvToArray(String input, Class<T> type) {
		aList<T> toList = resolveCsvToList(input);
		T[] t = (T[]) ArrayReflection.newInstance(type, 0);

		Array<T> result = new Array(t);
		for (int i = 0; i < toList.size(); i++)
			result.add(toList.get(i));
		return result;

	}

	public static Number[] resolveCsvToNumbers(String input) {
		int s = input.length() + 1;

		aList<String> toList = resolveCsvToList(input);
		Number[] trans;
		if (toList.isEmpty()) {
			trans = new Number[s];
			for (int i = 0; i < s; i++)
				trans[i] = 0;
			return trans;
		} else {
			trans = new Number[toList.size()];
			for (int i = 0; i < toList.size(); i++)
				trans[i] = parseNum(toList.get(i));
		}
		return trans;
	}

	public static aVector resolveCsvToVector(String input) {

		aList<String> toList = resolveCsvToList(input);
		Number[] trans = new Number[toList.size()];
		for (int i = 0; i < toList.size(); i++)
			trans[i] = parseNum(toList.get(i));

		aVector result = new aVector(input);
		return result;
	}

	public static aList<String> parseCsvToTypeTokens(String input) {

		aList<String> r = resolveCsvToList(input, "[:_]");

		aList<String> res = new aList<String>();
		for (int i = 0; i < r.size(); i++) {

			res.append("<" + r.get(i) + ">");
		}

		return res;
	}

	public static Object[] parseCSV(String input, String delimiter) {
		aList L = new aList();

		String starts = "({[<";
		String ends = ")}]>";

		boolean block = false;
		String blockTypeI = "";
		String blockTypeO = "";

		int segments = 1;
		aList<Integer> segmentsIndex = new aList<Integer>();
		aMap<String, aVector> blocksIndex = new aMap<String, aVector>(); // <String, aValue.Range>

		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == ',') {

				segments++;
				segmentsIndex.append(i);
			}
			if (starts.contains("" + input.charAt(i))) {
				block = true;
				blockTypeI = "" + input.charAt(i);
				int o = starts.indexOf(blockTypeI);
				blockTypeO = "" + ends.charAt(o);
			}
			while (block) {
				for (int j = i; j < input.length(); j++) {

					if (blockTypeO.equals("" + input.charAt(j))) {
						blocksIndex.put(blockTypeI + blockTypeO, new aVector(i, j));
						block = false;
						break;
					}
				}
			}

		}

		Log("~" + segments);
		Log(segmentsIndex);
		Log(blocksIndex.toLog());

		return L.toArray();
	}

	public static aMap<String, String> parseBlocks(String s) {
		aMap<String, String> Res = new aMap<String, String>();
		// String[] S = s.split("\\(([^()]*)\\)");
		// Log(s.split("\\[(.*?)\\]"));
		// Log(s.split("\\(([^()]*)\\)"));
		Pattern E = Pattern.compile("\\[(.*?)\\]");
		Matcher mE = E.matcher(s);
		while (mE.find())
			Res.put("[]", mE.group(1));

		Pattern W = Pattern.compile("\\((.*?)\\)");
		Matcher mW = W.matcher(s);
		while (mW.find())
			Res.put("()", mW.group(1));

		Pattern A = Pattern.compile("\\{(.*?)\\}");
		Matcher mA = A.matcher(s);
		while (mA.find())
			Res.put("{}", mA.group(1));

		Pattern F = Pattern.compile("\\<(.*?)\\>");
		Matcher mF = F.matcher(s);
		while (mF.find())
			Res.put("{}", mF.group(1));

		return Res;

	}

	public static String stripEndcaps(String in) {
		String starts = "({[<";
		String ends = ")}]>";

		StringBuilder process = new StringBuilder(in);

		if (ends.contains("" + in.charAt(in.length() - 1)))
			process.deleteCharAt(in.length() - 1);
		if (starts.contains("" + in.charAt(0)))
			process.deleteCharAt(0);
		return process.toString();
	}

	public static boolean listContains(iCollection<String> C, String S) {
		for (String s : C)
			if (s.contains(S))
				return true;

		return false;
	}

	public static aList<String> getContains(iCollection<String> C, String S) {
		aList<String> out = new aList<String>();
		for (String s : C)
			if (s.contains(S))
				out.append(s);
		return out;
	}

}
