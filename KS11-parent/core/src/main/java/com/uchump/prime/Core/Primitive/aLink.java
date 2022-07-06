package com.uchump.prime.Core.Primitive;

import static com.uchump.prime.Core.uAppUtils.*;

import java.math.BigDecimal;

import com.uchump.prime.Core.Math.Primitive.aNumber;
import com.uchump.prime.Core.Math.Utils.Maths;
import com.uchump.prime.Core.Primitive.A_I.iDisposable;
import com.uchump.prime.Core.Primitive.Struct._Map.Entry;
import com.uchump.prime.Core.Primitive.Struct.aMap;
import com.uchump.prime.Core.Primitive.Struct.aSet;
import com.uchump.prime.Core.Utils.StringUtils;

public class aLink extends aSet<aNode>/* aSet<zEntry<Number,zNode>> */ implements iDisposable {

	// aSet of Nodes described by this link
	// <LABEL>*[count]{contextTarget<V> % contextValue<K>}
	// count only if >1

	// Entry<Num,Node>

	protected static String[] dSymbols = new String[] { "->", "=>", "<=", "<-" }; // direction
	protected static String[] iSymbols = new String[] { "<", "{", "[", "(" };
	protected static String[] oSymbols = new String[] { ">", "}", "]", ")" };

	protected String dSymbol = dSymbols[0];
	public String iSymbol = iSymbols[0]; // in
	public String oSymbol = oSymbols[0]; // out

	public Object context;
	public String label;

	public Number phase;// numeric index
	protected aNode source;

	int max = -1;

	// public zType type = new zType("");

	public aLink(aNode source, String label) {
		super();
		this.source = source;
		this.label = label;

	}

	public aLink(aNode source, String label, aNode target) {
		this(source, label);
		this.append(target);
	}

	public aLink(aNode source, String label, Object target) {
		this(source, label, new aNode(target));
	}

	public aLink(aNode source, String label, aNode target, Object context) {
		this(source, label, target, context, 0);
	}

	public aLink(aNode source, String label, aNode target, Object context, Number phase) {
		this(source, label);
		this.context = context;
		this.append(phase, target);
	}

	public aLink setMax(int max) {
		this.max = max;
		return this;
	}

	public boolean isOpen() {
		if (this.max == -1)
			return true;
		if (this.size() >= max)
			return false;
		else
			return true;
	}

	@Override
	public void append(aNode n) {

		super.append(new Entry(0, n));
	}

	public void append(Number phase, aNode n) {
		super.append(new Entry(phase, n));
	}

	public void override(int index, aNode to) {
		this.get(index).set(to);
	}

	public void override(aNode to) {
		this.get(0).set(to);
	}

	public void remove(aNode n) {
		if (this.contains(n))
			super.remove(this.indexOf(n));

	}

	@Override
	public aNode get(Integer index) {
		return this.get(index, false);
	}

	public aNode get(int index, boolean phased) {
		Object O = super.get(index);

		Entry<Number, aNode> E = (Entry<Number, aNode>) O;
		if (E == null)
			return null;
		if (phased)
			return E;

		return E.getValue();
	}

	public aNode getNearest(Number phase) {
		int atIndex = 0;

		return this.get(atIndex);
	}

	// Maths.minIndex
	public aNode getLowest(Number phase) { // phase threshold
		// Log("XXX >!>");
		if (this.size() == 1)
			return this.get(0);

		for (int i = 0; i < this.size(); i++) {
			Entry<Number, aNode> E = (Entry<Number, aNode>) this.get(i, true);

			if (Maths.compare(E.getKey(), phase) >= 0) {
				return this.get(i);
			}
		}

		return this.get(0);
	}

	@Override
	public boolean contains(aNode other) {
		for (aNode N : this) {
			if (N == other || N.equals(other) || N.get() == other || N.get().equals(other))
				return true;
			if (N.get() == other.get() || N.get().equals(other.get()))
				return true;
		}
		return false;
	}

	@Override
	public void clear()
	{
		super.clear();
		if (this.source != null && this.source.links.containsValue(this))
			this.source.links.remove(new Entry<Object, String>(this.context, this.label));
		this.source = null;
		
	}
	
	public void dispose() {
		this.max = -1;
		this.phase = 0;
		this.context = null;
		this.dSymbol = dSymbols[0];
		this.iSymbol = iSymbols[0];


		this.clear();
	}

	public aLink cpy(aNode to) {
		aLink Z = new aLink(to, this.label, this.context);

		return Z;
	}

	public boolean compareContext(Object other) {

		String a = toIdString(other);
		String b = toIdString(this.context);
		if (a.equals(b))
			return true;
		else
			return false;
	}

	public boolean equals(Object context, String as) {
		if ((this.label.equals(as) || this.label.toLowerCase().equals(as.toLowerCase()))
				&& (this.context == context || this.context.equals(context)))
			return true;

		return false;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;

		if (other instanceof aLink) {
			aLink L = (aLink) other;
			/*
			 * if (this.label.toUpperCase().equals(L.label.toUpperCase()) && (this.context
			 * == L.context || this.context.equals(L.context))) return true;
			 */
			if (StringUtils.isFormOf(this.label, L.label) && this.context.equals(L.context))
				return true;
		}

		if (other instanceof Class) {
			Class C = (Class) other;
			if (this.label.toUpperCase().equals(C.getSimpleName().toUpperCase()))
				return true;
		}

		if (other instanceof String) {
			String S = "" + other;
			// if (this.label.equals(S) || this.label.toUpperCase().equals(S.toUpperCase())
			// || this.toString().equals(S)
			// || this.toString().toUpperCase().equals(S.toUpperCase()))
			if (StringUtils.isFormOf(this.label, S))
				return true;
		}

		return false;

	}

	public String toLabel() {
		return "<" + this.label + ">";
	}

	@Override
	public String toString() {
		String l = " | <" + this.label + ">@[" + (toHashId(this));
		// con = "";
		if (this.context != null)
			return l += "]%[" + this.context.getClass().getSimpleName() + toHashId(this.context) + "] | ";

		l += " | ";
		return l;
	}

	@Override
	public String toLog() {

		String ind = "";

		String log = ind + this.toString();
		log += " " + this.dSymbol + " "; // default =>

		if (this.phase != null)
			log += "#" + this.phase;

		if (this.data != null) {
			log += "{";
			for (int i = 0; i < this.size(); i++) {
				aNode n = this.get(i);
				log += n;

				if (i != this.size() - 1)
					log += ",";
			}
			log += "}";
		}
		return log;
	}

}
