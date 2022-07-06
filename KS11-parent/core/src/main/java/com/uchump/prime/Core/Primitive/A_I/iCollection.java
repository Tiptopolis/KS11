package com.uchump.prime.Core.Primitive.A_I;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.uchump.prime.Core.Primitive.Struct._Array;
import com.uchump.prime.Core.Primitive.Struct.aLinkedList;
import com.uchump.prime.Core.Primitive.Struct.aLinkedSet;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Primitive.Struct.aSet;
import com.uchump.prime.Core.Utils._Collections;

public interface iCollection<T> extends iIndex<T> {

	public void append(T entry); // add lol

	public void appendAll(T... entries);// addAll lol

	public default void appendAll(iGroup<Number, T> e) {
		for (Object o : e)
			this.append((T) o);
	}

	public void setAt(int at, T to); // replace item @index with E to

	@Override
	public default iCollection with(T... members) {
		this.appendAll(members);
		return this;
	}

	@Override
	public default iCollection with(iGroup other) {
		this.appendAll(other);
		return this;
	}

	public default Object getDefault(int index, T def) {
		T e = this.get(index);
		if (e == null)
			return def;
		else
			return e;
	}

	public void swap(int i, int j);

	public default void fill(int to, T val) {
		this.clear();
		for (int i = 0; i < to; i++)
			this.append(val);
	}

	public default iCollection<T> filled(int to, T val) {
		this.clear();
		for (int i = 0; i < to; i++)
			this.append(val);

		return this;
	}

	public default iCollection cpy() {
		iCollection c = this.newEmpty();
		for (Object o : this)
			c.append(o);

		return c;
	}

	public default Iterator<T> iterator() {
		return new _Array.ArrayIterator<T>(this);
	}

	public default int compareTo(Object other) {

		if (other instanceof iGroup) {
			int a = this.size();
			int b = ((iGroup) other).size();
			if (a > b)
				return 1;
			if (a < b)
				return -1;

			return 0;
		}
		return 0;
	}

	public T[] getComponentData();

	public default Class<T> getComponentType() {
		if (!this.isEmpty())
			return (Class<T>) this.get(0).getClass();
		return (Class<T>) this.getComponentData().getClass().getComponentType();
	}

	@SuppressWarnings("unchecked")
	static <E> E elementAt(Object[] es, int index) {
		return (E) es[index];
	}

	public default int modCount() {
		return 0;
	}

	public default void forEach(Consumer<? super T> action) {
		Objects.requireNonNull(action);
		final int expectedModCount = modCount();
		final Object[] es = getComponentData();
		final int size = this.size();
		for (int i = 0; modCount() == expectedModCount && i < size; i++)
			action.accept(elementAt(es, i));
		if (modCount() != expectedModCount)
			throw new ConcurrentModificationException();
	}


	

	public default aList<T> toList() {
		aList<T> l = new aList<T>();
		l.appendAll((iGroup) this);
		return l;
	}

	public default aSet<T> toSet() {
		aSet<T> l = new aSet<T>();
		l.appendAll((iGroup) this);
		return l;
	}

	public default aLinkedList<T> toLinkedList() {
		aLinkedList<T> l = new aLinkedList<T>();
		l.appendAll((iGroup) this);
		return l;
	}

	public default aLinkedSet<T> toLinkedSet() {
		aLinkedSet<T> l = new aLinkedSet<T>();
		l.appendAll((iGroup) this);
		return l;
	}

	public default String toCSV() {
		String l = "{";
		for (int i = 0; i < this.size(); i++) {
			l += this.get(i);
			if (i != this.size() - 1)
				l += ",";
		}
		l += "}";
		return l;
	}

	public default String toLog() {
		String p = "{}";
		if (!this.isEmpty())
			p = "{...}";

		String s = this.getClass().getSimpleName() + "[" + this.size() + "]" + p;

		return s;
	}

	static void ___OPERATIONS___() {

	}
	
	// these should be Consumers lol
	public default iCollection<T> newEmpty() {
		if (this instanceof aList)
			return new aList<T>();
		if (this instanceof aSet)
			return new aSet<T>();
		if (this instanceof aLinkedList)
			return new aLinkedList<T>();
		if (this instanceof aLinkedSet)
			return new aLinkedSet<T>();

		return new _Array<T>();
	}


	// pos = up, neg = down, wraps
	public default iCollection<T> shiftBy(int offset) {
		int s = this.size();

		// pos
		if (offset > 0)
			for (int i = 0; i < offset; i++) {
				T t = this.take(i);
				this.append(t);
			}

		// neg
		if (offset < 0)
			for (int i = offset; i < 0; i++) {
				T t = this.take(this.size() - 1);
				this.insert(0, t);
			}

		return this;
	}

	//////
	// add //^a+b //Merger of two objects into one
	
	public default iCollection<T> add(iCollection objects) {
		return this.union(objects);
	}

	
	public default iCollection<T> add(T... objects) {
		_Array<T> o = new _Array<T>(objects);

		return this.add(o);
	}
	
	@Override
	public default iGroup join(iGroup other) {
		return this.add((iCollection) other);
	}

	// join, a+b
	public default iCollection<T> union(iCollection<T> other) {
		for (T e : other)
			this.append(e);
		return this;
	}

	// sub //^a-b //Subtraction of one object from another
	public default iCollection<T> sub(iCollection objects) {
		return this.difference(objects);
	}

	public default iCollection<T> sub(T... objects) {
		_Array<T> o = new _Array<T>(objects);
		return this.sub(o);
	}

	public default iCollection<T> difference(iCollection<T> other) {
		iCollection<T> c = this.cpy();
		for (T e : this) {
			if (other.contains(e)) {
				// Log(this.indexOf(e) + ":>" + e);
				this.remove(e);
			}
		}

		return this;
	}

	public default iCollection<T> dif(iCollection<T> other) {
		return this.difference(other);
	}

	// mul //^a*b //Portions exclusive to both objects added together

	public default iCollection<T> exclusive(iCollection<T> other) {
		return this.hull(other);
	}

	public default iCollection<T> hull(iCollection<T> other) {
		iCollection<T> c = this.cpy();
		this.clear();
		for (T o : c)
			if (!other.contains(o))
				this.append(o);
		for (T o : other)
			if (!c.contains(o))
				this.append(o);

		return this;

	}

	public default iCollection<T> mul(iCollection<T> other) {
		return this.hull(other);
	}

	// div //^a/b //Portion common to both objects
	public default iCollection<T> intersection(iCollection<T> other) {

		iCollection<T> c = this.cpy();
		this.clear();
		for (T o : c)
			if (other.contains(o))
				this.append(o);
		for (T o : other)
			if (c.contains(o))
				this.append(o);

		return this;
	}

	public default iCollection<T> div(iCollection<T> other) {
		return this.intersection(other);
	}

	public default iCollection<T> sort() {
		return _Collections.Sort(this);
	}

	public default iCollection<T> sort(Comparator<T> c) {
		return _Collections.Sort(this, c);
	}

	public default iCollection<T> reverse() {
		return _Collections.Reverse(this);
	}

	public default <C extends iCollection<T>> C where(Predicate... P) {
		// generates a new collection based on predicate

		iCollection<T> c = this.newEmpty();

		for (Predicate p : P)
			c = this.where(p);

		return (C) c;
	}

	public default <C extends iCollection<T>> C where(Predicate P) {
		// generates a new collection based on predicate
		iCollection<T> c = this.newEmpty();

		for (T e : this)
			if (P.test(e))
				c.append(e);

		return (C) c;
	}

	public default iCollection filter(Predicate... P) {
		// prunes the calling collection
		iCollection<T> c = this.where(P);
		this.clear();
		this.appendAll(c.toArray());
		return this;
	}

	
	public static<X> iCollection<X> Consolidate(iCollection<X>...C)
	{
		_Array<X> res = new _Array<X>();
		for(iCollection c : C)
			res.appendAll(c);
		return res;
	}
	
}
