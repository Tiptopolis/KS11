package com.uchump.prime.Metatron.Lib.exp4M.tokenizer;

import java.util.function.Supplier;

import com.uchump.prime.Core.Math.N_Operator;
import com.uchump.prime.Core.Primitive.Struct.aMap;
import com.uchump.prime.Core.Utils.StringUtils;
import com.uchump.prime.Metatron.Lib.exp4j.operator.Operator;

public abstract class Tokens {
	
	
	

	public static String getType(_Token T) {
		return _Token.TYPES.get(T.getType());
	}

	public String getName(_Token T) {
		String n = "";

		return n;
	}

	public static class Open extends _Token<Open> {

		public Open() {
			super(_Token.TYPES.indexOf("PARENTHESES_OPEN"));
		}

	}

	public static class Close extends _Token<Close> {

		public Close() {
			super(_Token.TYPES.indexOf("PARENTHESES_CLOSE"));
		}

	}

	public static class _Word extends _Token<String> {

		public aMap<String, Supplier<String>> CASE;

		public _Word(String word) {
			super(_Token.TYPES.indexOf("WORD"));
			this.get = StringUtils.toName(word);
			Supplier<String> upper = () -> this.get.toUpperCase();
			Supplier<String> lower = () -> this.get.toLowerCase();

			this.CASE.put("UPPER", upper);
			this.CASE.put("LOWER", lower);
		}

		@Override
		public boolean equals(Object other) {
			if (other instanceof CharSequence) {
				return StringUtils.isFormOf(this.get, "" + other);
			}
			return false;
		}

		public String getCase(String c) {
			if (this.CASE.containsKey(c))
				return this.CASE.get(c).get();
			else
				return this.get;
		}

		@Override
		public String toString() {
			return "[" + this.get() + "]";
		}
	}

	public static class _Number extends _Token<Number> {

		public _Number(Number val) {
			super(_Token.TYPES.indexOf("NUMBER"));
			this.get = val;
		}

		public Number getValue() {
			return this.get;
		}

		@Override
		public boolean equals(Object other) {
			if (other instanceof Number) {
				return N_Operator.isEqual(this.getValue(), (Number) other);
			}
			return false;
		}
	}

	public static class Variable extends _Token<Variable> {
		protected String name = "";

		public Variable(String name) {
			super(_Token.TYPES.indexOf("VARIABLE"));
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}

	public static class Operator extends _Token<Operator> {

		public Operator(Operator op) {
			super(_Token.TYPES.indexOf("OPERATOR"));
		}

	}

	public static class Function extends _Token<Function> {

		public Function(Function fn) {
			super(_Token.TYPES.indexOf("FUNCTION"));
		}

	}

}
