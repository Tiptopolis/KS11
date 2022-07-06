package com.uchump.prime.Core.Primitive;

import static com.uchump.prime.Core.uAppUtils.*;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iGroup;
import com.uchump.prime.Core.Primitive.A_I.iMap;
import com.uchump.prime.Core.Primitive.A_I.iNode;
import com.uchump.prime.Core.Primitive.Struct._Map.Entry;
import com.uchump.prime.Core.Primitive.Struct.aDictionary;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Primitive.Struct.aMap;
import com.uchump.prime.Core.Primitive.Struct.aSetMap;
import com.uchump.prime.Core.Primitive.Struct.aSet;

public class aNode<T> implements iNode<T> {

	public static aNode<Void> Null = new aNode(Void.class);

	public String label;
	public T value;
	public Object type;
	public Supplier<T> Get = () -> {
		return this.get();
	};
	public Consumer<T> Set = (T t) ->{this.set(t);};
	
	public BiConsumer<String,Object> Put = (String s,Object o) ->{this.set(s, o);};

	public aSetMap<String, aDictionary> data; // Data, Inner, Outter

	// dictionary & links
	protected aDictionary<aNode> links;
	public aSetMap<String, Object> shared;

	public aNode() {
		this.value = (T) Void.class;
		this.shared = new aSetMap<String, Object>();
		this.data = new aSetMap<String, aDictionary>();
	}

	public aNode(T value) {
		this.value = value;
		this.shared = new aSetMap<String, Object>();
		this.data = new aSetMap<String, aDictionary>();
	}

	@Override
	public T get() {
		return this.value;
	}

	public Object get(String name) {
		return this.shared.get(name);
	}

	public aDictionary getData(String name) {
		return this.data.get(name);
	}

	@Override
	public void set(T to) {
		this.value = to;
	}

	public void set(String k, Object v) {
		this.shared.put(k, v);
	}

	@Override
	public <String> void label(String label) {
		this.label = (java.lang.String) label;
	}

	@Override
	public String label() {
		if (this.label == null)
			return this.hashId();
		else
			return this.label;
	}

	// [source->target]
	public aLink link(Object context, String as, aNode target) {
		return this.link(context, as, target, 0);
	}

	// [source <-> target]
	public aLink link(Object context, String as, String to, aNode target) {
		return this.link(context, as, to, target, 0, 0);
	}

	// [source->target] @ phase
	public aLink link(Object context, String as, aNode target, Number phase) {

		if (this.links == null)
			this.links = new aDictionary<aNode>();

		aLink L = null;
		Entry<Object, String> E = null;
		if (!this.links.containsKey(context, as)) {

			L = new aLink(this, as, target, context, phase); // phase goes here, <LL> :/
			E = new Entry<Object, String>(context, as);

			this.links.put(context, as, L);

		} else {
			E = new Entry<Object, String>(context, as);
			L = (aLink) this.links.get(E);
			L.append(phase, target);
		}
		return L;
	}

	public aLink link(Object context, String as, String to, aNode target, Number phaseAs, Number phaseTo) {

		target.link(context, to, this, phaseTo);

		aLink L = this.link(context, as, target, phaseAs);
		return L;
	}

	public aLink link(aLink link) {
		if (this.links == null)
			this.links = new aDictionary<aNode>();

		this.links.put(new Entry(this, link.label), link);
		return link;
	}

	public aList<aMap.Entry> getLinks() {

		aList<aMap.Entry> out = new aList<aMap.Entry>();
		for (Object o : this.links)
			out.append((aMap.Entry) o);

		return out;
	}

	public aList<aNode> getLinked(String as) {

		aList<aNode> res = new aList<aNode>();

		if (this.links != null && !this.links.isEmpty())
			for (Object E : this.links) {

				aMap.Entry e = (aMap.Entry) E;

				boolean a = e.getKey().equals(as) || e.getKey().equals(as.toUpperCase())
						|| e.getKey().equals(as.toLowerCase());
				boolean b = e.getValue().equals(as) || e.getValue().equals(as.toUpperCase())
						|| e.getValue().equals(as.toLowerCase());
				if (a || b)
					res.append(e);
			}
		if (res.isEmpty())
			return null;

		return res;
	}

	public void disconnect(aNode target) {
		aList<Entry<Object, String>> toDispose = new aList<Entry<Object, String>>();

		for (Entry E : this.links.getEntries()) {
			Entry<Object, String> K = (Entry<Object, String>) E.getKey();
			aLink V = (aLink) E.getValue();

			if (V.contains(target))
				V.remove(V.indexOf(target));
			if (V.isEmpty())
				toDispose.append(K);
		}
		for (Entry<Object, String> D : toDispose) {
			this.links.remove(D);
		}
		if (this.links.isEmpty())
			this.links = null;
	}

	public void disconnect(Object context) {
		aList<Entry<Object, String>> toDispose = new aList<Entry<Object, String>>();

		for (Entry E : this.links.getEntries()) {
			Entry<Object, String> K = (Entry<Object, String>) E.getKey();
			aLink V = (aLink) E.getValue();
			if (K.getKey() == context || K.getKey().equals(context)) {
				V.clear();
				toDispose.append(K);
			}

		}
		for (Entry<Object, String> D : toDispose) {
			this.links.remove(D);
		}
		if (this.links.isEmpty())
			this.links = null;
	}

	public void disconnect(Object context, String as) {

		aList<Entry<Object, String>> toDispose = new aList<Entry<Object, String>>();

		for (Entry E : this.links.getEntries()) {
			Entry<Object, String> K = (Entry<Object, String>) E.getKey();
			aLink V = (aLink) E.getValue();

			if ((V.context == context || V.context.equals(context)) && V.label.equals(as)) {
				V.clear();
			}
			if (V.isEmpty())
				toDispose.append(K);

		}
		for (Entry<Object, String> D : toDispose) {
			this.links.remove(D);
		}
		if (this.links.isEmpty()) {
			this.links = null;
		}

	}

	public boolean has(String as) {

		if (this.links != null && !this.links.isEmpty())
			for (Object E : this.links) {

				if (E.equals(as) || E.equals(as.toLowerCase()) || E.equals(as.toUpperCase()))
					return true;

				aMap.Entry e = (aMap.Entry) E;

				boolean a = e.getKey().equals(as) || e.getKey().equals(as.toUpperCase())
						|| e.getKey().equals(as.toLowerCase());
				boolean b = e.getValue().equals(as) || e.getValue().equals(as.toUpperCase())
						|| e.getValue().equals(as.toLowerCase());
				if (a || b)
					return true;
			}

		if (this.shared.containsKey(as) || this.shared.containsKey(as.toLowerCase())
				|| this.shared.containsKey(as.toUpperCase()))
			return true;

		return false;
	}

	public boolean has(Object context) {
		if (this.links != null || !this.links.isEmpty())
			for (Entry<Object, String> E : this.links.getKeys()) {
				if (E.getKey() == context || E.getKey().equals(context))
					return true;
			}
		return false;
	}

	public boolean has(Object context, String as) {

		if (this.shared.contains(as, context))
			return true;

		if (this.links == null || this.links.isEmpty())
			return false;

		for (Entry<Object, String> E : this.links.getKeys()) {
			if ((E.getKey() == context || E.getKey().equals(context))
					&& (E.getValue().equals(as) || E.getValue().toLowerCase().equals(as.toLowerCase())))
				return true;
		}
		return false;
	}

	public boolean has(Entry<Object, String> key) {

		if (this.shared.contains(key.getValue(), key.getKey()))
			return true;

		if (this.links == null || this.links.isEmpty())
			return false;

		return this.links.containsKey(key);
	}

	public boolean has(aNode target) {

		if (this.shared.containsValue(target))
			return true;

		if (this.links == null || this.links.isEmpty())
			return false;

		return this.links.containsValue(target);
	}

	public aLink getLink(Object context, String label) {
		if (this.links == null)
			this.links = new aDictionary<aNode>();

		return (aLink) this.links.get(context, label);
	}

	public aMap<String, Object> search(String... terms) {
		return iMap.search(this.shared, terms);
	}

	@Override
	public int compareTo(Object other) {

		// if(other instanceof )

		return 0;
	}

	public aNode fromTrivial(iGroup from) {
		if (from.size() == 1) {
			Object fI = from.get(from.firstIndex());
			if (fI instanceof aNode)
				return (aNode) fI;
			else
				return new aNode(fI);
		} else
			return null;
	}
	
	public iGroup toTrivial()
	{
		return new aSet<aNode>(this);
	}

	public String toTag() {
		String tag = "";

		tag = this.get().getClass().getSimpleName();
		return "<" + tag + ">";
	}

	public String toNodeTag() {
		String value = "";
		if (this.get() != this)
			value = "" + this.get();

		String c = "";
		if (this.get() instanceof iCollection || this.get() instanceof iMap || this.get() instanceof iGroup)
			c += "@" + this.get().hashCode();

		String v = "{(" + value + "):(" + this.toTag() + c + ")}]";
		return v;
	}

	@Override
	public String toString() {
		String s = "";
		String t = "";
		if (this.type != null)
			t = "" + this.type;

		s = this.label() + " = " + this.value + t;

		return s;
	}

	public String toLog() {
		String log = this.toString() + "\n";
		if (this.links != null)
			for (Object o : this.links) {
				Entry e = (Entry) o;
				log += " " + e.getKey() + "\n";
				aLink l = (aLink) e.getValue();
				log += l.toLog() + "\n";
			}
		return log;
	}

}
