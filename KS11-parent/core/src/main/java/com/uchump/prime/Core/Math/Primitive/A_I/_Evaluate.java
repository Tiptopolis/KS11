package com.uchump.prime.Core.Math.Primitive.A_I;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.uchump.prime.Core.Math.N_Operator;
import com.uchump.prime.Core.Math.Primitive.aNumber;
import com.uchump.prime.Core.Utils.StringUtils;


public interface _Evaluate{
	//0-args or / a == self
	public static _Evaluate.Modifier INC = new _Evaluate.Modifier("++",0f,(n)->{return N_Operator.add(n,1);});
	public static _Evaluate.Modifier ACC = new _Evaluate.Modifier("+=",0f,(n)->{return N_Operator.add(n,n);});
	
	//Mask
	public static class Modifier extends aNumber implements Function<Number,Number>, _Evaluate
	{

		public Function<Number,Number> doFn;
		private Number defaultValue = Float.NaN;
		protected Number value = Float.NaN;
		protected String symbol;

		public Modifier(String symbol, Number def,  boolean exe, Function<Number,Number> doF, Number val)
		{
			this.symbol = StringUtils.toName(symbol);
			this.defaultValue = def;
			this.value = val;
			this.doFn = doF;
			if(exe)
				this.value = this.apply(val);
			
		}
		
		public Modifier(String symbol, Number def, Function<Number,Number> doF)
		{
			this(symbol,def,false,doF,def);
		}
		//autocast lol
		public Modifier(Number val, Function<Number,Number> doF)
		{
			this("Result",N_Operator.resolveTo(0, val),true,doF,val);
		}

	
		@Override
		public Number apply(Number t) {			
			return  this.doFn.apply(t);		 
		}

		public Number applyTo(Number t)
		{
			t = this.doFn.apply(t);
			return t;
		}

		@Override
		public int intValue() {			
			return this.value.intValue();
		}

		@Override
		public long longValue() {
			return this.value.longValue();
		}

		@Override
		public float floatValue() {
			return this.value.floatValue();
		}

		@Override
		public double doubleValue() {
			
			return this.value.doubleValue();
		}
		
		@Override
		public String toString()
		{
			//return this.symbol + "Of["+ this.value+"]";
			return "["+ this.value+"]"+this.symbol;
		}

		
	}

	public static class Mask extends aNumber implements BiFunction<Number,Number,Number>
	{

		public BiFunction<Number,Number,Number> doFn;
		private Number defaultValue = Float.NaN;
		protected Number value = Float.NaN;
		protected String symbol;

		public Mask(String symbol, Number def,  boolean exe, BiFunction<Number,Number,Number> doF, Number val)
		{
			this.symbol = StringUtils.toName(symbol);
			this.defaultValue = def;
			this.value = val;
			this.doFn = doF;
			if(exe)
				this.value = this.apply(def,val);
			
		}
		
		public Mask(String symbol, Number def, BiFunction<Number,Number,Number> doF)
		{
			this(symbol,def,false,doF,def);
		}
		//autocast lol
		public Mask(Number val, BiFunction<Number,Number,Number> doF)
		{
			this("Result",N_Operator.resolveTo(0, val),true,doF,val);
		}

		@Override
		public Number apply(Number t, Number u) {
			
			return this.doFn.apply(t,u);
		}

		public Number applyTo(Number t)
		{
			t = this.doFn.apply(t, this.value);		
			return t;
		}
		
		public Number applyTo(Number t, Number u)
		{
			t = this.doFn.apply(t,u);		
			return t;
		}

		@Override
		public int intValue() {			
			return this.value.intValue();
		}

		@Override
		public long longValue() {
			return this.value.longValue();
		}

		@Override
		public float floatValue() {
			return this.value.floatValue();
		}

		@Override
		public double doubleValue() {
			
			return this.value.doubleValue();
		}
		
		@Override
		public String toString()
		{
			return this.symbol + "Of["+ this.value+"]";
		}



		
	}
	
	public static class Ratio extends aNumber implements Function<Number,Number>, _Evaluate{

		String S = ":";
		Number N = 1;
		Number D = 1;
		
		@Override
		public Number apply(Number t) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String toString()
		{
			return ""+ this.N+this.S+this.D;
		}
	}
	
	//optional
	public static class Potential<T> extends aNumber implements Function<Number,Number>, _Evaluate{
		//backbone of gradient values?
		//[}[]{]
		public Number minT;
		public Number maxT;
		public int sign;
		public _Evaluate A;
		protected Number value;
		@Override
		public Number apply(Number t) {
			
			if(this.A instanceof BiFunction)
			{
				this.value = (Number) ((BiFunction)A).apply(this.value, t);
			}
			if(this.A instanceof Function)
			{
				this.value = (Number) ((Function)A).apply(t);
			}
			return this.value;
		}

		@Override
		public int intValue() {
			this.value = this.apply(this.value);
			return this.value.intValue();
		}

		@Override
		public long longValue() {
			this.value = this.apply(this.value);
			return this.value.longValue();
		}

		@Override
		public float floatValue() {
			this.value = this.apply(this.value);
			return this.value.floatValue();
		}

		@Override
		public double doubleValue() {
			this.value = this.apply(this.value);
			return this.value.doubleValue();
		}
		
	}
}
