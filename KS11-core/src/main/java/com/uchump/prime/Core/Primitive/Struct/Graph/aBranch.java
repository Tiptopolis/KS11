package com.uchump.prime.Core.Primitive.Struct.Graph;

import com.uchump.prime.Core.Primitive.aNode;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iGroup;
import com.uchump.prime.Core.Primitive.A_I.iMap;
import com.uchump.prime.Core.Primitive.A_I.iNode;
import com.uchump.prime.Core.Primitive.A_I.iSequence;
import com.uchump.prime.Core.Primitive.Struct.aLinkedSet;

public class aBranch extends aNode<aBranch> implements iCollection<iNode>, iSequence<iNode> {

	protected aLinkedSet<iNode> data;

	@Override
	public void insert(Integer at, iNode member) {
		this.data.insert(at, member);

	}

	@Override
	public iNode get(Integer index) {

		return this.data.get(index);
	}

	@Override
	public void set(Integer i, iNode o) {
		this.data.set(i, o);
	}

	@Override
	public Integer indexOf(Object member) {
		return this.data.indexOf(member);
	}

	@Override
	public void remove(Integer at) {
		this.data.remove(at);
	}

	@Override
	public boolean contains(iNode entry) {
		if (this.data == null)
			return false;
		return this.data.contains(entry);
	}

	@Override
	public boolean isEmpty() {
		if (this.data == null)
			return true;
		return this.data.isEmpty();
	}

	@Override
	public int size() {
		return this.data.size();
	}

	@Override
	public iGroup resize(int to) {
		return this.data.resize(to);
	}

	@Override
	public void clear() {
		this.data.clear();
	}

	@Override
	public Integer getIndexType() {
		return 0;
	}

	@Override
	public iNode[] toArray() {
		int s = this.size();
		iNode[] N = new iNode[s];
		for (int i = 0; i < s; i++)
			N[i] = this.get(i);

		return N;
	}

	@Override
	public <N, X> iMap<N, X> toMap() {
		return this.data.toMap();
	}

	@Override
	public void append(iNode entry) {
		this.data.append(entry);

	}

	@Override
	public void appendAll(iNode... entries) {
		this.data.appendAll(entries);
	}

	@Override
	public void setAt(int at, iNode to) {
		this.data.setAt(at, to);
	}

	@Override
	public void swap(int i, int j) {
		// does swap even work in my LL?
		this.data.swap(i, j);

	}

	@Override
	public iNode[] getComponentData() {
		int s = this.size();
		iNode[] N = new iNode[s];
		for (int i = 0; i < s; i++)
			N[i] = this.get(i);
		return N;
	}

	@Override
	public int length() {
		return this.data.length();
	}

	@Override
	public iNode[] subSequence(int start, int end) {
		return this.data.subSequence(start, end);
	}

}
