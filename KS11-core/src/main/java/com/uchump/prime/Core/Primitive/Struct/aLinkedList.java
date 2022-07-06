package com.uchump.prime.Core.Primitive.Struct;

import static com.uchump.prime.Core.uAppUtils.*;

import java.util.Iterator;

import com.uchump.prime.Core.uAppUtils;
import com.uchump.prime.Core.Primitive.aLink;
import com.uchump.prime.Core.Primitive.aNode;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iGroup;
import com.uchump.prime.Core.Primitive.A_I.iLinkedCollection;
import com.uchump.prime.Core.Primitive.A_I.iNode;
import com.uchump.prime.Core.Primitive.A_I.iNodeIterator;
import com.uchump.prime.Core.Primitive.A_I.iSequence;

public class aLinkedList<T> implements iLinkedCollection<T> {

	// mapping single-relation sequences

	protected aNode<T> first;
	protected aNode<T> last;
	int size = 0;

	protected String pre = "PREV";
	protected String suc = "NEXT";

	public aLinkedList(Object... entries) {

		for (int i = 0; i < entries.length; i++) {
			this.append((T) entries[i]);
		}
	}
	
	@Override
	public Integer firstIndex() {
		return 0;
	}

	@Override
	public void append(T entry) {
		aNode newNode = null;

		if (!(entry instanceof aNode))
			newNode = new aNode<T>(entry);
		else
			newNode = (aNode) entry;

		if (this.first == null)
			this.first = newNode;

		aNode lst = this.last;
		this.last = newNode;

		if (this.first == this.last) {
			this.size++;
			return;
		}
		if (lst != null && lst != newNode) {
			lst.link(this, this.suc, this.last, this.size - 1);
			this.size++;
			this.last.link(this, this.pre, lst, this.size - 1);
		}

	}

	public void append(aNode newNode) {
		if (this.first == null)
			this.first = newNode;

		aNode lst = this.last;
		this.last = newNode;

		if (this.first == this.last) {
			this.size++;
			return;
		}

		if (lst != null && lst != newNode) {
			lst.link(this, this.suc, this.last, this.size - 1);
			this.size++;
			this.last.link(this, this.pre, lst, this.size - 1);
		}
	}

	@Override
	public void appendAll(T... entries) {
		for (int i = 0; i < entries.length; i++)
			this.append(entries[i]);
	}

	public void append(iCollection<T> entries) {
		for (int i = 0; i < entries.size(); i++)
			this.append(entries.get(i));
	}

	@Override
	public void insert(Integer atIndex, T entry) {

		aNode at = null;
		aNode newNode = null;

		if (!(entry instanceof aNode))
			newNode = new aNode<T>(entry);
		else
			newNode = (aNode) entry;

		this.insert(newNode, atIndex);
	}

	public void insert(aNode<T> newNode, int atIndex) {
		Object o = this.get(0);
		aNode N = (aNode) o;

		aList pred = new aList();
		aList post = new aList();

		for (int i = 0; i < atIndex; i++)
			pred.append(this.get(i));
		for (int i = atIndex; i < this.size; i++)
			post.append(this.get(i));

		this.clear();
		// Log("X123 zzz " + this.size);

		for (Object a : pred) {
			aNode A = (aNode) a;
			// Log("##A " + A.toLog());
			this.append(A);
		}
		this.append(newNode);
		for (Object b : post) {
			aNode B = (aNode) b;
			// Log("##B " + B.toLog());
			this.append(B);
		}
	}

	public void replace(T entry, int atIndex) {

		aNode at = null;
		aNode newNode = null;

		if (!(entry instanceof aNode))
			newNode = new aNode<T>(entry);
		else
			newNode = (aNode) entry;

		this.replace(newNode, atIndex);
	}

	public void replace(aNode<T> newNode, int atIndex) {

		aNode at = null;

		if (atIndex >= this.size()) {
			this.append(newNode);
			return;
		} else if (atIndex <= 0) {
			at = this.first;

			newNode.link(this, this.suc, at);
			at.link(this, this.pre, newNode);
			this.first = newNode;
			this.size++;
		} else {
			at = (aNode) this.get(atIndex);

			aNode Prv = (aNode) at.getLink(this, this.pre).getLowest(atIndex); // at's current Prev

			aLink atPrv_Next = (aLink) Prv.getLink(this, this.suc); // link pointing from Prev->at, override to newNode
			aLink atPrv = (aLink) at.getLink(this, this.pre);

			newNode.link(this, this.pre, Prv);
			newNode.link(this, this.suc, at);

			atPrv_Next.override(newNode);
			this.size++;
		}

	}

	@Override
	public void setAt(int at, T to) {
		iNode Z = (iNode) this.get(at);
		Z.set(to);
	}

	public void setLinkString(String next, String prev) {
		this.pre = next;
		this.suc = prev;
	}

	@Override
	public T get(Integer index) {

		int c = 0;
		for (T t : this) {
			if (c == index)
				return t;
			c++;
		}

		return null;
	}

	public aNode<T> getFirst() {
		return this.first;
	}

	public aNode<T> getLast() {
		return this.last;
	}

	@Override
	public Integer indexOf(Object object) {
		int c = 0;
		for (T t : this) {
			if (object == t)
				return c;
			c++;
		}

		return -1;

	}

	@Override
	public void remove(Integer index) {
		// remove & relink

		if (index < 0 || index > this.size() - 1)
			return;

		aNode at = (aNode) this.get(index);
		if (at == this.first) {
			// get first.next
			// set first = first.next
			// relink
			if (this.first.has(this, this.suc)) {
				aNode N = this.first.getLink(this, this.suc).getLowest(index);
				this.first.disconnect(this, this.suc);
				N.disconnect(this, this.pre);
				this.first = N;

			}
			this.size--;
		} else if (at == this.last) {
			if (this.last.has(this, this.pre)) {
				aNode P = this.last.getLink(this, this.pre).getLowest(index);
				this.last.disconnect(this, this.pre);
				P.disconnect(this, this.suc);
				this.last = P;

			}
			this.size--;
		} else {
			aNode atPrv = (aNode) at.getLink(this, this.pre).getLowest(index);
			aNode atNxt = (aNode) at.getLink(this, this.suc).getLowest(index);

			((aLink) atPrv.getLink(this, this.suc)).override(0, atNxt);
			((aLink) atNxt.getLink(this, this.pre)).override(0, atPrv);
			at.disconnect(this, this.pre);
			at.disconnect(this, this.suc);
			this.size--;
		}

	}

	@Override
	public boolean contains(T entry) {
		for (int i = 0; i < this.size(); i++) {
			aNode N = (aNode) this.get(i);
			if (this.get(i) == (entry) || N.equals(entry) || N.get().equals(entry))
				return true;
		}

		for (T t : this)
			if (entry == t)
				return true;

		return false;
	}

	@Override
	public boolean isEmpty() {
		return this.size() == 0;
	}

	@Override
	public int size() {
		return this.size;
	}

	public int count() {
		int c = 0;
		for (Object N : this)
			c++;

		return c;
	}

	@Override
	public void clear() {
		int s = this.size() - 1;
		for (int i = 0; i < s; i++)
			this.remove(0);

		this.size = 0;
		this.last = null;
		this.first = null;

	}

	public aLinkedList branchAt(int index, aLinkedList l) {
		Log(" **********  " + this);

		int i = Math.min(index, this.size);
		Log(i + " //    " + this.get(i));
		aNode N = (aNode) this.get(i);
		if (index == 0)
			N = this.first;
		if (index == -1)
			N = this.last;

		aNode F = l.first;

		N.link(l, this.suc, F, index + 1);
		F.link(this, this.pre, F, 0);
		return l;

		// lst.link(this, "NEXT", this.last, this.size - 1);
		// this.size++;
		// this.last.link(this, "PREVIOUS", lst, this.size - 1);
	}

	public aLinkedList link(aLinkedList l) {
		this.last.link(l, "NEXT", l.first, this.size);
		l.first.link(this, "PREVIOUS", this.last, -1);

		return this;
	}

	public aLinkedList extend(aLinkedList l) {
		for (Object o : l)
			this.append(l);

		return this;
	}

	public aLinkedList setRel(String prev, String next) {
		this.pre = prev;
		this.suc = next;
		return this;
	}

	@Override
	public aLinkedList<T> cpy() {
		aLinkedList<T> L = new aLinkedList<T>();
		for (int i = 0; i < this.size(); i++)
			L.append(this.get(i));

		return L;
	}

	public aList<T> toList() {
		aList<T> asList = new aList<T>();
		for (T t : this) {
			asList.append(t);
		}
		return asList;
	}

	public aList<T> toValueList() {
		aList<T> asList = new aList<T>();
		for (T t : this) {
			asList.append((T) ((aNode) t).get());
		}
		return asList;
	}

	@Override
	public T[] toArray() {

		T[] result = (T[]) new Object[this.size()];
		for (int i = 0; i < this.size(); i++) {
			result[i] = this.get(i);
		}
		return result;
	}

	@Override
	public Iterator<T> iterator() {
		return new LinkIterator(this);
	}

	@Override
	public Class getComponentType() {
		if (this.first != null)
			return this.first.getClass();
		else
			return Object.class;
	}

	@Override
	public String toString() {
		if (this.first != null && this.last != null) {
			String s = "";
			s += this.first.toNodeTag() + " => " + this.last.toNodeTag();

			return s;
		}
		return uAppUtils.toIdString(this);

	}

	public String toLog() {
		String log = this.toString() + "\n";

		if (this.first == null || this.last == null || this.isEmpty())
			return log;
		for (T t : this) {
			log += "[" + this.indexOf(t) + "]" + this.get(this.indexOf(t)) + "\n";
			// log += t + "\n";
		}
		return log;
	}

	public class LinkIterator<T> implements iNodeIterator<T> {

		private aLinkedList<T> array;

		int atIndex = -1;
		private aNode current;
		private aNode next;

		private String n;
		private String p;

		public LinkIterator(aLinkedList<T> array) {
			this.array = array;
			this.next = this.array.first;
			this.n = array.suc;
			this.p = array.pre;
		}

		public LinkIterator(aLinkedList<T> array, aNode at) {
			this.array = array;
			this.next = at;
			this.n = array.suc;
			this.p = array.pre;
		}

		public LinkIterator(aLinkedList<T> array, aNode at, String link) {
			this.array = array;
			this.next = at;
			this.n = link;
			this.p = link;
		}

		public LinkIterator(aLinkedList<T> array, aNode at, String next, String prev) {
			this.array = array;
			this.next = at;
			this.n = next;
			this.p = prev;
		}

		@Override
		public boolean hasNext() {

			if (this.atIndex == this.array.size - 1)
				return false;

			if (current == null && next != null)
				return true;

			if (current.getLinks() == null)
				return false;

			if (current.has(this.array, this.n))
				return true;

			if (this.array.isEmpty())
				return false;

			return false;
		}

		@Override
		public aNode<T> next() {

			if (this.hasNext()) {
				this.current = this.next;

				aLink L = this.current.getLink(this.array, this.n);

				if (L != null) {
					this.next = L.getLowest(this.atIndex);
					atIndex++;
				}

				return current;

			}

			return null;
		}

	}

	@Override
	public void swap(int i, int j) {

		aList list = this.toList();
		list.swap(i, j);
		this.clear();
		this.append(list);

	}

	@Override
	public iCollection<T> sort() {
		aList<T> L = this.toValueList();
		this.clear();
		L = (aList<T>) L.sort();
		this.append(L);

		return this;
	}

	@Override
	public T[] getComponentData() {
		T[] out = (T[]) new Object[this.size()];
		for (int i = 0; i < this.size; i++)
			out[i] = this.get(i);
		return out;
	}

	@Override
	public iGroup join(iGroup other) {
		for (Object o : other)
			this.append((T) o);
		return this;
	}

	@Override
	public void set(Integer i, T o) {
		this.setAt(i, o);

	}

	@Override
	public iGroup resize(int to) {
		return this;
	}

	@Override
	public int length() {
		return this.size;
	}

	@Override
	public T[] subSequence(int start, int end) {
		int dif = end - start;
		T[] out = (T[]) new Object[end - start];
		for (int i = 0, j = start; i < dif; i++, j++)
			out[i] = this.get(j);

		return out;

	}

	@Override
	public <N, X> aSetMap<N, X> toMap() {
		
		return null;
	}

}
