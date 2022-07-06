package com.uchump.prime.Core.Utils;

import static com.uchump.prime.Core.uAppUtils.*;
import static com.uchump.prime.Core.uSketcher.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.uchump.prime.Core.Math.Primitive.aVector;
import com.uchump.prime.Core.Primitive.aNode;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.Struct.aList;



public abstract class _Collections {

	public static Object takeNearest(int index, iCollection things) {
		int i = Math.min(things.size(), index);
		return things.take(i);
	}

	public static iCollection Sort(iCollection col) {
		ArrayList L = new ArrayList();
		for (Object o : col)
			L.add(o);
		Collections.sort(L);

		col.clear();
		for (int i = 0; i < L.size(); i++)
			col.append(L.get(i));

		return col;

	}

	public static <T> iCollection<T> Sort(iCollection<T> col, Comparator<T> c) {
		ArrayList<T> L = new ArrayList<T>();
		for (Object o : col)
			L.add((T) o);
		Collections.sort(L, c);

		col.clear();
		for (int i = 0; i < L.size(); i++)
			col.append(L.get(i));

		return col;
	}

	public static iCollection Reverse(iCollection col) {
		ArrayList L = new ArrayList();
		for (Object o : col)
			L.add(o);
		Collections.reverse(L);

		col.clear();
		for (int i = 0; i < L.size(); i++)
			col.append(L.get(i));

		return col;

	}

	public static aList MergeSort(iCollection... items) {
		aList res = new aList();
		for (int I = 0; I < items.length; I++) {
			items[I].sort();
			iCollection C = items[I];
			for (int i = 0; i < items[I].size(); i++)
				res.append(C.get(i));

		}
		return res;
	}
	
	public static aList MergeSort(Comparator c, iCollection... items) {
		aList res = new aList();
		for (int I = 0; I < items.length; I++) {
			items[I].sort(c);
			iCollection C = items[I];
			for (int i = 0; i < items[I].size(); i++)
				res.append(C.get(i));

		}
		return res;
	}

	public static Object vectGet(aVector<Integer> path, iCollection<iCollection> items) {
		iCollection current = new aList();
		int to = Math.min(path.size(), items.size());

		if (to >= 1) {
			for (int i = 0; i < to; i++) {
				int I = path.get(i).intValue();
				int S = items.get(i).size();

				Object c = items.get(i).get(Math.min(I, S));
				if (c instanceof aNode)
					c = ((aNode) c).get();
				// Log(Math.min(I, S)+"___ " + c);
				// current.append(c);
				if (c instanceof iCollection)
					current = (iCollection) c;
				else
					current.append(c);
			}
			if (current != null && current.size() > 1)
				return current;
			else
				return current.get(0);
		}

		return null;
	}

	public static <T> aList<T> consolidate(Object... material) {
		aList n = new aList();

		// pre
		/*
		 * for(Object o : material) { if(o instanceof iCollection)
		 * n.append(consolidate(o).toArray()); //iMap? else n.append(o);
		 * 
		 * }
		 */
		for (Object o : material) {
			if (o instanceof iCollection) {
				iCollection c = (iCollection) o;
				aList L = consolidate(c.toArray());
				n.append(L.toArray());
			} else
				n.append(o);
		}

		return n;
	}

	public static <T> aList<T> consolidateNodes(Object... material) {
		aList n = new aList();

		// pre
		/*
		 * for(Object o : material) { if(o instanceof iCollection)
		 * n.append(consolidate(o).toArray()); //iMap? else n.append(o);
		 * 
		 * }
		 */
		for (Object o : material) {
			if (o instanceof iCollection) {
				iCollection c = (iCollection) o;
				aList L = consolidate(c.toArray());
				n.append(L.toArray());
			} else
				n.append(o);
		}

		aList N = new aList();
		for (int i = 0; i < n.size(); i++) {
			Object o = n.get(i);
			if (o instanceof aNode)
				N.append(((aNode) o).get());
			else
				N.append(o);
		}

		return N;
	}

	public static iCollection subDiv(iCollection s, int to) {
		return subDiv(s, 0, to);
	}

	public static iCollection subDiv(iCollection s, int from, int to) {
		iCollection out = s.newEmpty();
		for (int i = from; i < to; i++)
			out.append(s.get(i));
		return out;
	}

	public static iCollection doSubDiv(iCollection s, int to) {
		return doSubDiv(s, 0, to);
	}

	public static iCollection doSubDiv(iCollection s, int from, int to) {
		iCollection out = s.newEmpty();
		for (int i = from; i < to; i++)
			out.append(s.get(i));

		s.clear();
		s.append(out);

		return s;
	}

}
