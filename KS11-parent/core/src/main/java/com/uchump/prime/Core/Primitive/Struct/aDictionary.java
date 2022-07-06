package com.uchump.prime.Core.Primitive.Struct;

import java.util.Iterator;

import com.uchump.prime.Core.Primitive.aLink;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iGroup;
import com.uchump.prime.Core.Primitive.A_I.iMap;
import com.uchump.prime.Core.Primitive.Struct._Map.Entry;

public class aDictionary<V> implements iMap<Entry<Object, String>, iCollection<V>> {

	public aMap<Entry<Object, String>, iCollection<V>> data = new aMap<Entry<Object, String>, iCollection<V>>();

	public V get(Object context, String as, int index) {

		return this.data.get(new Entry<Object, String>(context, as)).get(index);
	}

	@Override
	public Integer firstIndex() {
		return 0;
	}
	
	@Override
	public iCollection<V> get(Entry<Object, String> K) {
		return this.data.get(K);
	}

	public iCollection<V> get(Object context, String as) {
		return this.data.get(new Entry(context, as));
	}

	public void put(Object context, String as) {
		this.put(new Entry<Object, String>(context, as));
	}

	public void put(Object context, String as, V value) {
		iCollection<V> ent = this.get(context, as);
		// this.put(new Entry<Object, String>(context, as), value);
		if (ent != null)
			ent.append(value);
	}

	public void put(Object context, String as, V... values) {
		for (V v : values)
			this.put(new Entry<Object, String>(context, as), v);
	}

	public void put(Entry<Object, String> E, iCollection<V> val) {
		this.data.put(E, val);
	}

	public void put(Object context, String as, iCollection<V> val) {
		this.data.put(new Entry<Object, String>(context, as), val);
	}

	public boolean containsKey(Object context, String as) {
		return this.containsKey(new Entry<Object, String>(context, as));
	}

	@Override
	public boolean containsValue(Object val) {
		for (iCollection V : this.getValues()) {
			if (V.contains(val))
				return true;
		}

		return this.data.containsValue(val);
	}

	@Override
	public void put(Entry<Object, String> key, Object val) {
		this.data.put(key, val);
	}

	@Override
	public void put(Entry<Object, String> key, Object... val) {
		for (Object o : val) {
			this.put(key, o);
		}
	}

	public void remove(Entry<Object, String> key) {
		if (this.containsKey(key)) {
			for (int i = 0; i < this.data.size(); i++) {
				Entry<Object, String> K = this.getKeys().get(i);
				if (key.equals(K) || key == K) {
					this.data.keys.remove(i);
					this.data.values.remove(i);
				}
			}
		}
	}

	@Override
	public boolean contains(Entry<Object, String> key, Object val) {
		return this.data.contains(key, val);
	}

	@Override
	public iCollection<Entry<Object, String>> getKeys() {
		return this.data.getKeys();
	}

	@Override
	public iCollection<iCollection<V>> getValues() {
		return this.data.getValues();
	}

	@Override
	public Iterator<Entry<Entry<Object, String>, iCollection<V>>> iterator() {
		return this.data.iterator();
	}

	@Override
	public boolean isEmpty() {
		return this.data.isEmpty();
	}

	@Override
	public int size() {
		return this.data.size();
	}

	@Override
	public void clear() {
		this.data.clear();

	}

	public String toLog() {
		String log = this.getClass().getSimpleName() + "{" + this.data.keys.size() + "}\n";
		log += "{\n";
		for (int i = 0; i < this.data.size(); i++) {
			if (this.getValues().get(i) instanceof aLink) {
				log += this.getKeys().get(i) + ":" + ((aLink) this.getValues().get(i)).toLog();
			} else {
				log += this.getKeys().get(i) + ":" + this.getValues().get(i);
			}
			if (i != this.data.size() - 1)
				log += ", \n";
		}

		log += "\n}";
		return log;
	}

	@Override
	public iCollection<iCollection<V>> getAll(Entry<Object, String> key) {

		aSet<iCollection<V>> S = new aSet<iCollection<V>>();

		S.append(this.get(key));

		if (S.isEmpty())
			return null;
		else
			return S;
	}

	@Override
	public iGroup join(iGroup other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(Integer at, Entry<Entry<Object, String>, iCollection<V>> member) {
		// TODO Auto-generated method stub

	}

	@Override
	public Entry<Entry<Object, String>, iCollection<V>> get(Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void set(Integer i, Entry<Entry<Object, String>, iCollection<V>> o) {
		// TODO Auto-generated method stub

	}



	@Override
	public void remove(Integer at) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean contains(Entry<Entry<Object, String>, iCollection<V>> entry) {
		if (this.containsKey(entry.getKey())) {
			return this.containsValue(entry.getValue());
		} else
			return false;
		
	}

	@Override
	public iGroup resize(int to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public iGroup with(Entry<Entry<Object, String>, iCollection<V>>... members) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public iGroup with(iGroup other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entry<Entry<Object, String>, iCollection<V>>[] toArray() {
		return this.data.toArray();
	}

	@Override
	public  iMap<Entry<Object, String>, V> toMap() {
		aMap<Entry<Object, String>, V> M = new aMap<Entry<Object, String>, V>();
		
		for(Entry<Entry<Object, String>, iCollection<V>> E : this.data)
		{
			iCollection<V> V = iCollection.Consolidate(E.getValue());
			M.put(E.getKey(),V);
		}
		
		return M;
	}



}
