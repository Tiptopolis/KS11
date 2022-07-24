package com.uchump.prime.Core.Primitive.Struct;

import static com.uchump.prime.Core.uAppUtils.*;


import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.Struct._Map.Entry;
import com.uchump.prime.Core.Utils.StringUtils;

public class aDictionary<V> extends aMultiMap<Entry<Object, String>, V> {

	public void put(D_Key key, Object val) {
		if (!this.contains(key, val)) {
			if (this.containsKey(key))
				key = (aDictionary<V>.D_Key) this.keys.get(this.keys.indexOf(key));
			this.keys.append(key);
			this.values.append((V) val);
		}
	}

	@Override
	public void put(Entry<Object, String> key, Object val) {
		if (!this.contains(key, val)) {
			if (this.containsKey(key))
				key = (D_Key) this.keys.get(this.keys.indexOf(key));
			this.keys.append((D_Key) key);
			this.values.append((V) val);
		}
	}

	public void put(Object context, String label, V as) {

		D_Key D = newEntry(context, label);

		if (!this.getAll(D).contains(as)) {
			this.keys.append(D);
			this.values.append(as);
		}
	}

	public void put(Object context, String label, V... as) {

		for (V v : as)
			this.put(context, label, v);
	}

	@Override
	public Entry<Entry<Object, String>, V> get(Integer index) {
		if (this.size() == 0)
			return null;
		return this.getEntries().get(index);
	}

	public Object get(Object context, String as) {
		return this.getAll(new Entry(context, as));
	}

	public Object get(String get) {
		Object[] E = this.getAllOf(get).toArray();
		Log(get+" >>>>>>>>>>>>>>>>>> " +E.length);
		Log(E);
		if (E == null || E.length == 0)
			return null;

		if (E.length == 1)
			return E[0];
		
		aList out = new aList();
		for(Object o : E)
			if(o.toString().contains(get) || o.toString().equals(get))
				out.append(o);
		return out;
	}

	public iCollection<Entry<Entry, Object>> getAllOf(String... get) {

		aMultiMap<Entry<Object, String>, Object> S = new aMultiMap<Entry<Object, String>, Object>();

		for (String g : get) {
			for (Entry E : this) {
				if (D_Key.is(E, g)) {
					S.put(E, this.getAll(E));
				}
			}
		}

		aList O = new aList();
		for (Entry E : S)
			O.append(E.getKey());
		return O;

	}
	
	@Override
	public boolean contains(Entry<Object, String> k, Object v)
	{
		return super.contains(k,v);
	}
	
	public boolean contains(Object context, String as, V val)
	{
		Entry<Object, String> key = new Entry<Object, String>(context, as);
		return this.contains(key,val);
	}
	
	public boolean contains(Object context, String as)
	{
		return this.containsKey(context,as);
	}

	public boolean containsKey(Object context, String as) {
		return this.containsKey(new Entry<Object, String>(context, as));
	}

	public void remove(Entry<Object, String> key) {
		if (this.containsKey(key)) {
			for (int i = 0; i < this.size(); i++) {
				Entry<Object, String> K = this.getKeys().get(i);
				if (key.equals(K) || key == K) {
					this.keys.remove(i);
					this.values.remove(i);
				}
			}
		}
	}

	/*@Override
	public String toLog() {
		String log = this.getClass().getSimpleName() + "{" + this.keys.size() + "}\n";
		log += this.keys.toSet().size() + "x" + this.values.size() + "\n";

		String LHS = "";
		String pLHS = "";
		for (Entry<Entry<Object, String>, V> E : this.getEntries()) {
			D_Key D = (aDictionary<V>.D_Key) E.getKey();
			V V = (V) E.getValue();
			// String A = ""+D.getKey();
			// String B = D.getValue();
			// String C = ""+E;

			String A = "";
			String B = "";
			String C = "";

			pLHS = LHS;
			LHS = "" + D.getKey();
			if (D.getKey().equals(pLHS))
				A = "";
			else
				A = "" + D.getKey();

			B = D.getValue();
			C = "" + V;
			String i = StringUtils.indent(LHS.length());

			if (A.equals(""))
				A = i;

			log += A + " " + B + " " + C + "\n";
		}

		return log;
	}*/

	@Override
	public D_Key newEntry(Object key, Object val) {
		return new D_Key(key, "" + val);
	}

	public class D_Key extends _Map.Entry<Object, String> {
		public D_Key(Object k, String v) {
			super(k, v);
		}

		public boolean is(String label) {
			if (StringUtils.isFormOf(this.getValue()).test(label))
				return true;
			if(StringUtils.contains(""+this.getKey()).test(label))
				return true;
			return false;
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
}