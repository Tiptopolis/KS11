package com.uchump.prime.Core.Primitive.Struct;

import static com.uchump.prime.Core.uAppUtils.*;

import java.util.Iterator;

import com.uchump.prime.Core.uAppUtils;
import com.uchump.prime.Core.Primitive.aLink;
import com.uchump.prime.Core.Primitive.aNode;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iGroup;
import com.uchump.prime.Core.Primitive.A_I.iNode;
import com.uchump.prime.Core.Primitive.A_I.iNodeIterator;
import com.uchump.prime.Core.Primitive.A_I.iSequence;


public class aLinkedSet<T> extends aLinkedList<T>{

	@Override
	public void append(T entry) {
		aNode newNode = null;

		if (!(entry instanceof aNode))
			newNode = new aNode<T>(entry);
		else
			newNode = (aNode) entry;

		if (this.contains((T) newNode) || this.contains(entry))
			return;

		if (this.first == null)
			this.first = newNode;

		aNode lst = this.last;
		this.last = newNode;

		if (this.first == this.last) {
			this.size++;
			return;
		}
		if (lst != null && lst != newNode) {
			lst.link(this, "NEXT", this.last, this.size - 1);
			this.size++;
			this.last.link(this, "PREVIOUS", lst, this.size - 1);
		}

	}

	public void append(aNode newNode) {
		if (this.contains((T) newNode))
			return;
		if (this.first == null)
			this.first = newNode;

		aNode lst = this.last;
		this.last = newNode;

		if (this.first == this.last) {
			this.size++;
			return;
		}

		if (lst != null && lst != newNode) {
			lst.link(this, "NEXT", this.last, this.size - 1);
			this.size++;
			this.last.link(this, "PREVIOUS", lst, this.size - 1);
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
	public void insert(Integer atIndex,T entry ) {

		aNode at = null;
		aNode newNode = null;

		if (this.contains((T) newNode))
			return;

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
			newNode.link(this, "NEXT", at);
			at.link(this, "PREVIOUS", newNode);
			this.first = newNode;
			this.size++;
		} else {
			at = (aNode) this.get(atIndex);

			aNode Prv = (aNode) at.getLink(this, "PREVIOUS").getLowest(atIndex); // at's current Prev

			aLink atPrv_Next = (aLink) Prv.getLink(this, "NEXT"); // link pointing from Prev->at, override to newNode
			aLink atPrv = (aLink) at.getLink(this, "PREVIOUS");

			newNode.link(this, "PREVIOUS", Prv);
			newNode.link(this, "NEXT", at);
			// atPrv.override(newNode);
			atPrv_Next.override(newNode);
			this.size++;
		}

	}
	

}
