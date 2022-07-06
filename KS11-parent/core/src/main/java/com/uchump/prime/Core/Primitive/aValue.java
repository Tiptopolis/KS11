package com.uchump.prime.Core.Primitive;

import static com.uchump.prime.Core.uAppUtils.*;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JFormattedTextField.AbstractFormatter;

import com.badlogic.gdx.math.MathUtils;
import com.uchump.prime.Core.Math.N_Operator;
import com.uchump.prime.Core.Math.Primitive.aVector;
import com.uchump.prime.Core.Math.Utils.Maths;
import com.uchump.prime.Core.Math.Utils.aGeom;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iMap;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Primitive.Struct.aMap;
import com.uchump.prime.Core.Primitive.Struct.aSetMap;
import com.uchump.prime.Core.Utils.StringUtils;

public class aValue<T> extends aMap.Entry<String, T> {

	// aLabeldValue lol, a meme
	// get() updates and returns last known value
	protected boolean exists = true;

	public Supplier<String> Name = () -> this.getKey();

	protected boolean mutable = true;
	
	public aValue(String name) {
		super(name, null);		
		this.init();
	}

	public aValue(String name, T val) {
		super(name, val);
		this.init();
		
		
	}

	public aValue(aValue<T> cpy) {
		super(cpy.label(), cpy.get());
		
		this.init();
	}

	public aValue init() {
		this.buildIn();
		this.buildOut();	
		
		return this;
	}
	protected void buildOut()
	{
		this.Get = () -> {
			return this.get();
		};
		
	}
	protected void buildIn()
	{
		this.Set = (T t) ->{this.set(t);};
		this.Put = (String s,Object o) ->{this.set(s, o);};
		
	}

	public T get(int i) {
		if (this.get() instanceof iCollection<?>)
			return ((iCollection<T>) this.get()).get(i);
		else if (this.get() instanceof iMap)
			return (T) ((iMap) this.get()).getValues().get(i);

		else
			return null;
	}
	
	public aValue evaluate()
	{
		//Override me lol
		return this;
	}
	


	@Override
	public String label() {
		return this.getKey();
	}

	@Override
	public String toString() {
		String s = "";

		String c = "";
		String g = "";
		if (this.get() != null)
			c = this.get().getClass().getSimpleName();

		if (this.get() != this && this.get() != null)
			g = "" + this.get();

		s = this.label() + "<" + c + ">" + " = " + g;

		return s;
	}

	@Override
	public boolean equals(Object other) {

		if (super.equals(other))
			return true;
		else {
			if (other instanceof String) {
				String o = "" + other;
				return StringUtils.isFormOf(this.label(), o);
			}
		}

		return false;
	}

	@Override
	public String toLog() {

		String s = "";
		String value = "";
		String t = "";
		if (this.get() != this)
			value = "" + this.get();

		s = "[" + this.label() + "@" + this.hashCode() + " = " + this.toNodeTag();
		if (this.shared != null)
			s += "\n" + this.shared;
		return s;
	}

	// new Reference, extends aFunction<aNode>
	public static class Reference extends aValue<aNode> {
		// value acting as a label for another value
		// aMap<<String>[](.csv(a,b,c,d,!,@,#,$)),<aValue>(v1,v2,v3,v4)>
		public Reference(String name, aNode val) {
			super(name, val);
			this.link(null, name, val);
		}

		public Reference(String name, Object val) {
			this(name, new aNode(val));
		}

		public Reference(String name, aNode val, Object context) {
			super(name, val);
			this.link(context, name, val);
		}

		public Reference(String name, Object val, Object context) {
			this(name, new aNode(val), context);
		}

		// returns the value of the linked node
		public Object pull() {
			return this.get().get();
		}

		public void push(Object thing) {
			if (thing instanceof aNode)
				this.push(((aNode) thing).get());
			else
				this.get().set(thing);
		}

		public Reference update(Object value) {
			this.push(value);
			return this;
		}

		public Object rtn(Object value) {
			return this.update(value).pull();
		}

		public aMap.Entry<Reference, aNode> enter() {
			return new aMap.Entry<Reference, aNode>(this, this.get());
		}

		public aMap.Entry<String, Object> toEntry() {
			return new aMap.Entry(this.label(), this.get().get());
		}

		@Override
		public String toString() {
			String value = "";
			String t = "";
			if (this.get() != this)
				value = "" + this.get();

			return "[" + this.label() + this.get().toTag() + "] = [" + this.pull() + "]";
		}
	}

	public static class Range extends aValue<Number> {

		// int range?
		public Number min = 0f;
		public Number max = 1f;

		private Number pin = 1f;

		public boolean mod = false;
		public boolean ref = false;
		private int dir = 1;

		public Range(String name, aVector val) {
			super(name, val.resize(2));
			if (val.get(0) != null)
				this.min = val.get(0);
			if (val.get(1) != null)
				this.min = val.get(1);

		}

		public Range(aVector val) {
			this("aRange", val);
		}

		public Range(String name, Number min, Number max) {
			super(name, new aVector(min, max));
			this.min = min;
			this.max = max;
			this.pin(this.max);
			this.set(min.floatValue());
		}

		public Range(Number min, Number max) {
			this("aRange", min, max);
		}

		@Override
		public void set(Number val) {

			if (this.mod) {
				this.value = N_Operator.resolveTo(N_Operator.mod(val, this.max), val);
			} else
				this.value = N_Operator.resolveTo((N_Operator.clamp(val, this.min.floatValue(), this.max.floatValue())),
						val);

		}

		public Range pin(Number format) {
			this.pin = format;
			return this;
		}

		public Range repin(Number format) {
			this.pin = format;
			this.value = N_Operator.pin(this.get(), format);

			return this;
		}

		// mode-set

		// modulator
		public Range mod() {
			this.mod = !this.mod;
			this.ref = false;
			return this;
		}

		// reflector, inop
		public Range ref() {
			this.mod = false;
			this.ref = !this.ref;
			this.dir *= -1;
			return this;
		}

		// util
		public boolean contains(Number n) {
			boolean gMin = n.floatValue() >= this.min.floatValue();
			boolean gMax = n.floatValue() <= this.max.floatValue();
			return (gMin && gMax);
		}

		public float distance() {
			return this.max.floatValue() - this.min.floatValue();
		}

		public float dst() {
			return this.distance();
		}

		public Number getMidpoit() {
			return this.reterp(0.5f);
		}

		public Range step(Number by) {
			//
			Number expectRes = N_Operator.add(N_Operator.mul(by, this.dir), this.get());
			// Log(" ___" + expectRes);
			Number overrun = 0;
			if (this.ref && (Maths.compare(expectRes, this.max) == 1) || Maths.compare(expectRes, this.min) == -1) {
				overrun = N_Operator.resolveTo( N_Operator.mod(by, this.max),by);

				// Log(" ***" + overrun);
				this.dir *= -1;
			}

			this.set(N_Operator.add(this.get(), N_Operator.mul(N_Operator.sub(by, overrun), this.dir)));
			return this;
		}

		// return value% of this range
		public Number interp(Number value) {
			Number N = N_Operator.lerp(this.min.floatValue(), this.max.floatValue(), value.floatValue());
			return N_Operator.resolveTo(N,value);
		}

		public Number percentOf(Number value) {
			return this.interp(value);
		}

		// value is what % of this range
		public Number reterp(Number value) {
			Number N = N_Operator.ilerp(this.min.floatValue(), this.max.floatValue(), value.floatValue());
			return N_Operator.resolveTo(N, value);
		}

		public Number percentIs(Number value) {
			return this.reterp(value);
		}

		@Override
		public String toString() {
			/*
			 * String s = ""; s += this.getClass().getSimpleName(); s += "{" + this.min +
			 * "->" + this.max + "}:[" + this.get + "]|[" +
			 * this.reterp(this.get.floatValue()) + "]";
			 * 
			 * if(this.mod) s+="%";
			 */
			String s = "";
			String md = "%";

			if (this.mod)
				md = "%";
			if (this.ref)
				md = "$";

			String dr = "->";
			if (this.dir < 0)
				dr = "<-";

			Number Pin = N_Operator.pin(this.get(), this.pin);
			Number Per = N_Operator.pin(this.reterp(this.get().floatValue()), this.pin);

			s += "{" + this.min + dr + this.max + "}" + md + "[" + Pin + "|" + Per + "]";

			return s;
		}
	}

	//
	public static class Axial<T> extends aValue<aMap<Integer, T>> {

		// -2 > tiny
		// -1 > small
		// 1 > normal
		// 2 > big
		//

		// aNode.Context
		protected aSetMap<Integer, Object> terms = new aSetMap<Integer, Object>();

		public Axial(String name, T[] items) {
			super(name, new aMap<Integer, T>());
			int s = items.length / 2;

			aVector[] addresses = aGeom.interpolate(new aVector(0), new aVector(s));
		}

	}

}
