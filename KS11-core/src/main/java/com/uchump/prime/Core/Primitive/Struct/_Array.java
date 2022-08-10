package com.uchump.prime.Core.Primitive.Struct;

import static com.uchump.prime.Core.uAppUtils.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import com.uchump.prime.Core.Math.N_Operator;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iGroup;

public class _Array<T> implements iCollection<T> {

	protected T[] data;
	protected int modCount = 0;
	protected Class type = Object.class;

	public Supplier<Boolean> isTrivial = () -> {
		return this.size() == 1;
	};

	public _Array() {
		this.data = (T[]) new Object[0];
	}

	public _Array(T... entries) {
		this();
		for (T t : entries)
			this.append(t);
	}

	public _Array(iGroup entries) {
		this();
		this.join(entries);
	}

	public _Array(Collection<T> c) {
		this();
		for (T t : c)
			this.append(t);
	}

	@Override
	public int modCount() {
		return this.modCount;
	}

	@Override
	public Class<T> getComponentType() {
		Class C = this.type;
		for (T o : this)
			if (instanceOf(C).test(o))
				C = o.getClass();

		this.type = C;
		return this.type;
	}

	@Override
	public iGroup join(iGroup other) {

		for (Object o : other)
			this.append((T) o);

		return this;
	}

	@Override
	public void append(T entry) {
		this.modCount++;
		int i = data.length + 1;
		this.resize(i);
		data[i - 1] = entry;
	}

	@Override
	public void appendAll(T... entries) {
		for (int i = 0; i < entries.length; i++) {
			this.append(entries[i]);
		}
	}

	@Override
	public void insert(Integer at, T entry) {

		// this.clear();
		_Array<T> pre = new _Array<T>();
		_Array<T> pst = new _Array<T>();
		for (int i = 0; i < at; i++) {
			pre.append(this.get(i));
		}

		for (int j = at; j < this.size(); j++) {
			pst.append(this.get(j));
		}

		this.clear();
		for (int i = 0; i < pre.size(); i++) {
			this.append(pre.get(i));
		}

		this.append(entry);
		for (int j = 0; j < pst.size(); j++) {
			this.append(pst.get(j));
		}

	}

	public void set(iCollection<T> to) {
		this.clear();
		this.appendAll(to.toArray());

	}

	@Override
	public T[] getComponentData() {
		return this.data;
	}

	// @AT , #OF
	// @2#10->2
	// @12#10->2
	// @-2#10->8
	// @-12#10->8
	// @-15#10->5
	@Override
	public void setAt(int at, T to) {

		if (at < 0)
			at = (Math.abs(this.size() - at)) % this.size();
		if (at < this.size()) {
			// this.modCount++;
			this.data[at] = to;
		} else
			this.append(to);
	}

	@Override
	public T get(Integer index) {
		if (data == null || data.length == 0)
			return null;
		T out = (T) data[this.resolveIndex(index)];
		return out;
	}

	@Override
	public void set(Integer i, T o) {
		/*
		 * if (i < this.size()) { this.modCount++; this.data[i] = o; } else
		 * this.append(o);
		 */
		this.modCount++;
		this.data[this.resolveIndex(i)] = o;
	}

	@Override
	public void swap(int i, int j) {
		if (i > this.size() || j > this.size())
			return;

		Object I = this.get(i);
		Object J = this.get(j);

		this.setAt(i, (T) J);
		this.setAt(j, (T) I);
	}

	@Override
	public boolean contains(T entry) {

		for (int i = 0; i <= this.data.length - 1; i++) {
			if (data[i] == entry || data[i].equals(entry))
				return true;
		}
		return false;
	}

	@Override
	public Integer indexOf(Object member) {
		for (int i = 0; i < this.data.length; i++) {
			if (data[i] == member)
				return i;
		}
		return -1;
	}

	@Override
	public void remove(Integer index) {
		this.modCount++;
		// anti-insert
		aList<T> result = new aList<T>();

		this.data[index] = null;
		for (int i = 0; i < this.size(); i++) {
			if (this.get(i) != null)
				result.append(this.get(i));
		}

		this.resize(0);

		for (int i = 0; i < result.size(); i++) {
			this.append(result.get(i));
		}
	}

	@Override
	public boolean isEmpty() {

		return this.data.length == 0;
	}

	private void grow(int by) {
		this.modCount++;
		data = Arrays.copyOf(data, data.length + by);
	}

	@Override
	public int size() {
		return this.data.length;
	}

	@Override
	public iGroup resize(int to) {
		data = Arrays.copyOf(data, to);
		return this;
	}

	@Override
	public void clear() {
		for (int i = 0; i <= this.size() - 1; i++) {
			this.remove(i);
		}
		this.resize(0);
		this.modCount = 0;
	}

	@Override
	public Iterator<T> iterator() {
		return new ArrayIterator<T>(this);
	}

	@Override
	public Integer getIndexType() {

		return 0;
	}

	@Override
	public T[] toArray() {
		return this.data;
	}

	@Override
	public String toString() {

		// String s = this.getClass().getSimpleName() + "{" + this.getSize() + "}\n";
		String s = "";
		s += "{";
		if (this.data != null)
			for (int i = 0; i < this.data.length; i++) {
				s += /* "[" + i + "]" + */this.data[i];
				if (i != this.data.length - 1)
					s += ",";
			}
		s += "}";
		return s;
	}

	public String toLog() {
		String log = this.getClass().getSimpleName() + "{" + this.size() + "}\n";
		if (this.data != null)
			for (int i = 0; i < this.data.length; i++) {
				log += "[" + i + "]" + this.data[i] + "\n";
			}
		return log;
	}

	public static class ArrayIterator<T> implements Iterator<T> {

		public iCollection<T> array;
		int index = -1;
		boolean valid = true;
		protected transient int modCount = 0;

		public ArrayIterator(iCollection<T> array) {
			this.array = array;
		}

		@Override
		public boolean hasNext() {

			if (!valid) {
				throw new RuntimeException("#iterator() cannot be used nested.");
			}
			return index < (array.size() - 1);
		}

		@Override
		public T next() {
			this.modCount++;
			if (index >= (array.size()))
				throw new NoSuchElementException(String.valueOf(index));
			if (!valid) {
				throw new RuntimeException("#iterator() cannot be used nested.");
			}
			index++;

			return array.get(index);
		}

		public T previous() {
			this.modCount++;
			if (index < 0 || index >= (array.size()))
				throw new NoSuchElementException(String.valueOf(index));
			if (!valid) {
				throw new RuntimeException("#iterator() cannot be used nested.");
			}
			index--;

			return array.get(index);
		}

		@Override
		public void remove() {
			this.modCount++;
			this.array.remove(this.index);
		}

	}

	@Override
	public aMap<Integer, T> toMap() {
		aMap<Integer, T> M = new aMap<Integer, T>();
		for (int i = 0; i < this.size(); i++)
			M.put(i, data[i]);
		return M;
	}

	public aMultiMap<Integer, T> toMultiMap() {
		aMultiMap<Integer, T> M = new aMultiMap<Integer, T>();
		for (int i = 0; i < this.size(); i++)
			M.put(i, data[i]);
		return M;
	}

}
