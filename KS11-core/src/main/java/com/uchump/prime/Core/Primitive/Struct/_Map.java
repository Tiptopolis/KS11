package com.uchump.prime.Core.Primitive.Struct;

import static com.uchump.prime.Core.uAppUtils.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.uchump.prime.Core.Primitive.aNode;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iGroup;
import com.uchump.prime.Core.Primitive.A_I.iMap;
import com.uchump.prime.Core.Primitive.A_I.iNode;
import com.uchump.prime.Core.Primitive.Struct._Map.Entry;
import com.uchump.prime.Core.Utils.StringUtils;

public abstract class _Map<K, V> implements iMap<K, V> {

	// index-mapped lists
	// 1-many[unique] mapping
	public iCollection<K> keys;
	public iCollection<V> values;

	public _Map() {
		this.keys = new aSet<K>();
		this.values = new aList<V>();
	}

	public _Map(iCollection<K> k, iCollection<V> v) {
		this.keys = k;
		this.values = v;
	}

	public _Map(Class<iCollection> k, Class<iCollection> v) {
		this.keys = iCollection.newEmpty(k);
		this.values = iCollection.newEmpty(v);
	}

	public _Map(AbstractMap<K, V> M) {
		this.keys = new aList<K>();
		this.values = new aList<V>();
		for (Object o : M.entrySet()) {
			Map.Entry<K, V> E = (Map.Entry<K, V>) o;
			this.put(E.getKey(), E.getValue());
		}
	}

	@Override
	public Integer firstIndex() {
		return 0;
	}

	@Override
	public void put(K key, Object val) {
		if (!this.contains(key, val)) {
			if(this.containsKey(key))
				key = (K) this.keys.get( this.keys.indexOf(key));
			this.keys.append(key);
			this.values.append((V) val);
		}
	}

	public void put(K key, Object... vals) {
		for (Object v : vals) {
			this.put(key, v);
		}
	}

	public void put(Entry<K, V> E) {
		this.put(E.getKey(), E.getValue());
		
	}

	public void put(Entry<K, V>... E) {
		for (Entry<K, V> v : E)
			this.put(E);
	}

	public void put(iCollection<Entry<K, V>> m) {
		for (Entry<K, V> E : m) {
			this.put(E);
		}
	}

	@Override
	public iCollection<V> getAll(K key) {
		aList<V> result = new aList<V>();


		for (int i = 0; i < this.size(); i++) {
			K k = this.keys.get(i);
			if (k == key || k.equals(key))
				result.append(this.values.get(i));
		}

		return result;
	}

	@Override
	public iGroup join(iGroup other) {
		if (other instanceof iMap) {
			iMap M = (iMap) other;
			for (Object e : M.getEntries()) {
				Entry E = (Entry) e;
				this.put(E);
			}
		}
		return this;
	}

	@Override
	public void insert(Integer at, Entry<K, V> member) {
		this.keys.insert(at, member.getKey());
		this.values.insert(at, member.getValue());

	}

	@Override
	public Entry<K, V> get(Integer index) {
		if (this.size() == 0)
			return null;
		return this.getEntries().get(index);
	}

	@Override
	public void set(Integer i, Entry<K, V> o) {
		this.keys.set(i, o.getKey());
		this.values.set(i, o.getValue());

	}

	@Override
	public Integer indexOf(Object member) {
		if (member instanceof _Map.Entry)
			return this.getEntries().indexOf(member);
		else
			return this.getKeys().indexOf(member);
	}

	@Override
	public void remove(Integer at) {
		this.removeKey(this.keys.get(at));

	}

	public void removeKey(K k) {
		/*
		 * aList<V> V = (aList<V>) this.getAll(k); aSet<Integer> indices = new
		 * aSet<Integer>(); for (V v : V)
		 * indices.append(this.values.indexOf(v).intValue()); indices.sort().reverse();
		 * 
		 * for(int i = indices.size()-1; i >0; i--)
		 * this.values.remove(indices.get(i).intValue());
		 * 
		 * this.keys.remove(k); this.keys.remove(this.keys.size() - 1);
		 */

		_Map M = this.cpy();
		this.clear();
		for (Object e : M.getEntries())
			if (e instanceof Entry) {
				Entry E = (Entry) e;
				if (E.getKey() != k && !E.getKey().equals(k))
					this.put(E);
			}

	}

	/*public void delete(int at) {
		this.remove(at);
	}

	public void delete(K k) {
		this.removeKey(k);
	}*/

	public boolean contains(K key, Object val) {
		boolean k = false;
		boolean v = false;

		for(Entry<K,V> E : this.getEntries())
		{
			if(E.getKey()==key||E.getKey().equals(key))
				k=true;
			if(E.getValue()==val || E.getValue().equals(val))
				v=true;
				
		}
		
		return k && v;
	}

	@Override
	public boolean contains(Entry<K, V> entry) {

		return this.getEntries().contains(entry);
	}

	@Override
	public boolean isEmpty() {
		return this.keys.isEmpty();
	}

	@Override
	public int size() {
		return this.keys.size();
	}

	@Override
	public iGroup resize(int to) {

		this.keys.resize(to);
		return this;
	}

	@Override
	public void clear() {
		this.keys.clear();
		this.values.clear();

	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		aSet entries = new aSet<Entry<K, V>>();
		iCollection v;
		for (int i = 0; i < this.keys.size(); i++) {
			v = this.getAll(this.keys.get(i));
			entries.append(new Entry<K, V>(this.keys.get(i), this.values.get(i)));
		}

		// return this.keys.iterator();
		return entries.iterator();
	}

	@Override
	public iCollection<K> getKeys() {
		return this.keys;
	}

	@Override
	public iCollection getValues() {
		return this.values;
	}

	@Override
	public String toString() {
		// String s = this.getClass().getSimpleName() + "{" + this.getSize() + "}\n";
		String s = "";
		s += "{";
		/*
		 * if (this.keys != null && !this.keys.isEmpty()) for (int i = 0; i <
		 * this.keys.size(); i++) { s += "[" + this.keys.get(i) + "|" +
		 * this.getAll(this.keys.get(i)) + "]"; if (i != this.keys.size() - 1) s += ",";
		 * }
		 */
		if (this.keys != null && !this.keys.isEmpty())
			for (K k : this.keys) {
				int i = this.keys.indexOf(k);
				s += "[" + k + "|" + this.getAll(k) + "]";
				if (i != this.keys.size() - 1)
					s += ",";
			}
		s += "}";
		return s;
	}

	public String basicLog() {
		String log = this.getClass().getSimpleName() + "{" + this.keys.size() + "}\n";
		for (int i = 0; i < this.keys.size(); i++) {
			log += "[" + i + "]" + this.keys.get(i) + "|" + this.values.get(i) + "\n";
		}

		return log;
	}

	public String toLog() {
		String log = this.getClass().getSimpleName() + "{" + this.keys.size() + "}\n";
		log += this.keys.toSet().size() + "x" + this.values.size() + "\n";
		/*
		 * for (int i = 0; i < this.keys.size(); i++) { log += "[" + i + "]" +
		 * this.keys.get(i) + "|" + this.values.get(i) + "\n"; }
		 */
		aSet<String> L = new aSet<String>();
		aSet<K> thisKeys = this.keys.toSet();
		for (K k : thisKeys) {
			int i = keys.indexOf(k);
			// for(int i =0; i < this.keys.size(); i++) {
			// K k = keys.get(i);
			L.append("->[" + i + "] " + k + " |" + this.getAll(k));
		}
		for (int i = 0; i < L.size(); i++)
			log += "[" + i + "]" + L.get(i) + "\n";

		return log;
	}

	public static class Entry<K, V> extends aNode<V> implements iNode<V> {

		protected K key;
		public Supplier<K> Key = () -> this.key;

		public Function<K, Entry<K, V>> setKey = (K k) -> {
			this.key = k;
			return this;
		};
		public Function<V, Entry<K, V>> setValue = (V v) -> {
			this.value = v;
			return this;
		};

		protected Entry() {
			this.key = null;
			this.value = null;
		}

		public Entry(K key, V val) {
			this.key = key;			
			this.value = val;
			this.label = (this.key + "|" + this.value);
		}

		public K getKey() {
			return this.key;
		}

		public V getValue() {
			return this.value;
		}

		public Entry<V, K> invert() {
			return new _Map.Entry<V, K>(this.value, this.key);
		}

		@Override
		public String toString() {
			this.label = (this.key + "|" + this.value);
			String s = "["+this.label+"]";
			return s;
		}

		@Override
		public boolean equals(Object other) {
			if (other == null)
				return false;
			if (other == this)
				return true;

			if (this.key instanceof CharSequence && other instanceof CharSequence) {
				return StringUtils.isFormOf("" + this.key, "" + other);
			}

			if (other instanceof Entry) {
				Entry E = (Entry) other;
				if ((E.getKey() == this.getKey() || E.getKey().equals(this.getKey()))
						&& (E.getValue() == this.getValue() || E.getValue().equals(this.getValue()))) {
					return true;
				}
				if (this.key instanceof CharSequence && other instanceof CharSequence) {
					return StringUtils.isFormOf("" + this.key, "" + other);
				}
			}

			return false;
		}

		@Override
		public int compareTo(Object other) {
			if (other instanceof _Map.Entry)
				return this.compareTo((Entry) other, keyComparator());
			else if (other instanceof Comparable && this.getKey() instanceof Comparable) {
				if (this.getKey().getClass().isAssignableFrom(other.getClass())
						|| other.getClass().isAssignableFrom(this.getKey().getClass())) {
					Comparable c1 = (Comparable) this.getKey();
					Comparable c2 = (Comparable) other;
					return c1.compareTo(c2);
				}
			}
			return 0;
		}

		public int compareTo(_Map.Entry other, Comparator how) {

			return how.compare(this, other);

		}

		public static <K> Comparator<K> keyComparator() {
			return (Comparator<K>) iMap.keyComparator();
		}

		public static <V> Comparator<V> valComparator() {
			return (Comparator<V>) iMap.keyComparator();
		}

	}

	@Override
	public iGroup with(Entry<K, V>... members) {

		for (Entry e : members)
			this.put(e);

		return this;
	}

	@Override
	public iGroup with(iGroup other) {

		if(other.isEmpty())
			return this;
		
		for (Object o : other) {
			if (o instanceof Entry)
				this.put((Entry) o);
			this.put((K) o, null);
		}

		return this;
	}

	public _Map<K, V> newEmpty() {
		if (this instanceof aMultiMap)
			return new aMultiMap<K, V>();
		if (this instanceof aMap)
			return new aMap<K, V>();

		return new aMap<K, V>();
	}

	public _Map<K, V> cpy() {
		_Map M = newEmpty();
		for (Entry E : this)
			M.put(E);
		return M;
	}

	@Override
	public Entry<K, V>[] toArray() {
		int s = this.getEntries().size();
		Object[] O = this.getEntries().toArray();
		Entry<K, V>[] out = new Entry[s];
		for (int i = 0; i < s; i++)
			out[i] = (Entry<K, V>) O[i];
		return out;
	}

	/////////////////////
	public static Properties pathProperties(String path) {
		// FileReader fr = new FileReader("src/main/resources/jdbcLOCAL.properties");
		Properties props = new Properties();
		String P = path += ".properties";
		try {
			FileReader fr = new FileReader(path);
			props.load(fr);
		} catch (IOException e) {

			e.printStackTrace();
		}
		return props;
	}

	public static _Map<String, Object> mapProperties(Properties props) {
		aMap<String, Object> res = new aMap<String, Object>();
		for (Object o : props.keySet()) {
			res.put("" + o, props.get(o));
		}
		return res;
	}

	public static Properties toProps(_Map<String, Object> M) {
		Properties props = new Properties();
		for (_Map.Entry E : M)
			props.put(E.getKey(), E.getValue());
		return props;

	}
	
	

}
