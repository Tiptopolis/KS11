package com.uchump.prime.Core.Primitive.Struct;

import static com.uchump.prime.Core.uAppUtils.*;

import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.Struct._Map.Entry;
import com.uchump.prime.Core.Utils.StringUtils;

public class aDictionary<V> extends aMultiMap<Entry<Object, String>, V> {
	public void put(D_Key key, Object val) {
		if (!this.contains(key, val)) {
			if (this.containsKey(key))
				key = (aDictionary.D_Key) this.keys.get(this.keys.indexOf(key));
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

	@Override
	public V get(Entry<Object, String> key) {
		if (this.getAll(key) != null)
			return this.getAll(key).get(0);
		else
			return null;
	}

	public Object get(Object context, String as) {
		//return this.getAll(new Entry(context, as));
		
		aSet all = new aSet(this.getAll(new Entry(context, as)));
		
		if(all.size()==1)
			return all.get(0);
		else {
			Log("!!       " + all);
			return all;
		}
	}

	public Object get(Object context, String... as) {

		aSet ent = new aSet(this.getAllOf(context));
		aSet out = new aSet();
		int s = ent.size();
		if (s == 1)
			return ent.get(0);
		//Log("!!       " + ent);
		for (String S : as)
			for (int i = 0; i < s; i++) {
				Entry E = (Entry) ent.get(i);
				if (StringUtils.isFormOf("" + E.getValue(), S))
					out.append(E);
			}
		if (out.size() == 1)
			return out.get(0);
		return out;

	}

	public Object getAllOf(String... as) {
		iCollection<Entry<Entry<Object, String>, V>> ent = this.getEntries();
		aSet out = new aSet();
		for (String S : as)
			for (int i = 0; i < ent.size(); i++) {
				for (String s : as) {
					Entry E = ent.get(i);
					Entry K = (Entry) E.getKey();

					if (K.getKey() == s || K.getKey().equals(s) || s.equals(K.getKey()))
						out.append(E);
					if (StringUtils.isFormOf("" + K.getValue(), S))
						out.append(E);
				}
			}
		return out;
	}

	public Object getAllOf(Object context) {

		if (context instanceof CharSequence)
			return this.getAllOf("" + context, "");

		iCollection<Entry<Entry<Object, String>, V>> ent = this.getEntries();

		aList out = new aList();

		for (int i = 0; i < ent.size(); i++) {
			Entry E = ent.get(i);
			Entry K = (Entry) E.getKey();

			if (K.getKey() == context || K.getKey().equals(context) || context.equals(K.getKey()))
				out.append(E);
		}

		if (out.isEmpty())
			return null;
		if (out.size() == 1)
			return out.get(0);
		return out;
	}
	
	public iCollection collect(Object context)
	{
		aSet ent = (aSet) new aSet().with(this.getAllOf(context));;
		
		return ent;
	}
	
	public iCollection collect(Object context, String...names)
	{
		aSet ent = (aSet) new aSet().with(this.get(context,names));
		
		return ent;
	}

	@Override
	public boolean contains(Entry<Object, String> k, Object v) {
		return super.contains(k, v);
	}

	public boolean contains(Object context, String as, V val) {
		Entry<Object, String> key = new Entry<Object, String>(context, as);
		return this.contains(key, val);
	}

	public boolean contains(Object context, String as) {
		return this.containsKey(context, as);
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

	@Override
	public D_Key newEntry(Object key, Object val) {
		return new D_Key(key, "" + val);
	}

	public static class D_Key extends _Map.Entry<Object, String> {

		public D_Key(Object k, String v) {
			super(k, v);
		}

		public boolean is(String label) {
			if (StringUtils.isFormOf(this.getValue()).test(label))
				return true;
			if (StringUtils.contains("" + this.getKey()).test(label))
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
