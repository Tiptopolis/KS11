package com.uchump.prime.Core.Math.Primitive;

import static com.uchump.prime.Core.Utils.StringUtils.commaLen;
import static com.uchump.prime.Core.uAppUtils.Log;

import com.badlogic.gdx.math.Matrix4;
import com.uchump.prime.Core.Math.N_Operator;
import com.uchump.prime.Core.Math.Utils.Maths;
import com.uchump.prime.Core.Math.Utils.aMatrixUtils;
import com.uchump.prime.Core.Math.Utils.aVectorUtils;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iGroup;
import com.uchump.prime.Core.Primitive.A_I.iLinkedCollection;
import com.uchump.prime.Core.Primitive.A_I.iMap;
import com.uchump.prime.Core.Primitive.A_I.iNode;
import com.uchump.prime.Core.Primitive.Struct.aSet;

public class aMatrix<N extends Number> extends Number implements iLinkedCollection<aVector<N>> {

	// collection of Numbers that manintains a solid 2d-structure
	// has aSetMap of Vector addresses underneath
	
	
	private aSet<aVector<N>> data;

	
	public aMatrix(aVector chart) {
		// aVector(10,10,10,10)
		// results in 4 vectors of size 10
		for (int i = 0; i < chart.size(); i++) {
			this.setAt(i, new aVector(commaLen(chart.get(i).intValue())));			
		}
	}

	public aMatrix(int m, int n) {
		if (m == 1 && n == 1)
			this.append(new aVector().filled(1, 0));
		else
			for (int i = 0; i < m; i++) {
				this.append(new aVector(commaLen(n)));
			}
	}

	public aMatrix(int n) {
		this(n, n);
	}

	public aMatrix(Number n) {
		this(n.intValue());

		for (int i = 0; i < this.size(); i++)
			this.get(i).toType(n);
	

	}

	// splits vector into n sub-vectors from given values
	public aMatrix(aVector values, int n) {
		this(n, values.toArray());
	}

	public aMatrix(int n, aVector values) {
		this(n, values.toArray());
	}

	// splits values into n vectors
	public aMatrix(int n, Number... values) {
		this(aVectorUtils.splitEvery(n, new aVector(values)));

	}

	public aMatrix(aVector... vects) {
		int length = vects.length;
		int size = aVectorUtils.longestSize(vects);
		for (int i = 0; i < length; i++) {
			aVector v = vects[i];
			v.resize(size);
			this.append(v);
		}

	}

	public aMatrix(iCollection<aVector> vects) {
		this(vects.toArray());

	}

	public aMatrix(Matrix4 matrix) {
		this(4, new aVector<Float>(matrix.val));
		this.adjoint();
	}

	@Override
	public aVector<N> get(Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void set(Integer i, aVector<N> o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void insert(Integer at, aVector<N> member) {
		// TODO Auto-generated method stub

	}

	@Override
	public Integer indexOf(Object member) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Integer at) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean contains(aVector<N> entry) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int length() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public iGroup resize(int to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}



	@Override
	public void append(aVector<N> entry) {
		// TODO Auto-generated method stub

	}

	@Override
	public void appendAll(aVector<N>... entries) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAt(int at, aVector<N> to) {
		// TODO Auto-generated method stub

	}

	@Override
	public void swap(int i, int j) {
		// TODO Auto-generated method stub

	}

	@Override
	public aVector<N>[] getComponentData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int intValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long longValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float floatValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double doubleValue() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	///////



	public aVector flatten() {
		aVector prime = this.get(0).cpy();
		for (int i = 0; i < this.size(); i++) {
			if (i != 0) {
				prime.append(this.get(i));
			}
		}

		return prime;
	}

	public aVector dimensions() {
		int width = aVectorUtils.longestSize(this.toArray());

		return new aVector(this.size(), width);
	}
	
	


	@Override
	public aVector<N>[] subSequence(int start, int end) {
		// TODO Auto-generated method stub
		return null;
	}
	public aMatrix cpy() {
		aMatrix copy = new aMatrix();
		for (int i = 0; i < this.size(); i++) {
			copy.append(this.get(i).cpy());
		}

		return copy;
	}

	
	@Override
	public aVector<N>[] toArray() {
		return this.data.toArray();
	}
	

	@Override
	public <N, X> iMap<N, X> toMap() {
		return (iMap<N, X>) this.data.toMap();
	}

	@Override
	public String toLog() {
		String log = this.getClass().getSimpleName() + "{" + this.size() + "x" + this.length() + "}\n";
		if (this.data != null)
			for (int i = 0; i < this.size(); i++) {
				log += "[" + i + "]" + this.get(i) + "\n";
			}
		return log;
	}



}
