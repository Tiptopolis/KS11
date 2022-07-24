package com.uchump.prime.Core.Math.Primitive;

import static com.uchump.prime.Core.Utils.StringUtils.commaLen;
import static com.uchump.prime.Core.uAppUtils.Log;

import com.badlogic.gdx.math.Matrix4;
import com.uchump.prime.Core.Math.M_Operator;
import com.uchump.prime.Core.Math.N_Operator;
import com.uchump.prime.Core.Math.V_Operator;
import com.uchump.prime.Core.Math.Utils.Maths;
import com.uchump.prime.Core.Math.Utils.aMatrixUtils;
import com.uchump.prime.Core.Math.Utils.aVectorUtils;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iGroup;
import com.uchump.prime.Core.Primitive.A_I.iLinkedCollection;
import com.uchump.prime.Core.Primitive.A_I.iMap;
import com.uchump.prime.Core.Primitive.A_I.iNode;
import com.uchump.prime.Core.Primitive.Struct._Map;
import com.uchump.prime.Core.Primitive.Struct.aSet;

public class aMatrix<N extends Number> extends Number implements iLinkedCollection<aVector<N>> {

	// collection of Numbers that manintains a solid 2d-structure
	// has aSetMap of Vector addresses underneath

	private aSet<aVector<N>> data = new aSet<aVector<N>>();

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

		return this.data.get(index);
	}
	
	public Number getAt(aVector address) {

		int mX = 0;
		int mY = address.get(1).intValue();
		if (address.get(0) != null)
			mX = address.get(0).intValue();
		if (address.get(1) != null)
			mY = address.get(1).intValue();

		aVector V = this.get(mX);

		return V.get(mY);
	}

	@Override
	public void set(Integer i, aVector<N> o) {
		this.data.set(i, o);
	}

	// takes an Address Vector
	public void set(aVector<Integer> key, Number val) {
		int indA = key.get(0).intValue();
		int indB = key.get(1).intValue();
		aVector address = this.get(indA);
		address.setAt(indB, val);
	}

	public void set(aMatrix m) {
		// aMatrix M = m.cpy();

		this.clear();
		for (int i = 0; i < m.size(); i++) {
			this.append(m.get(i).cpy());
		}

	}

	@Override
	public void insert(Integer at, aVector<N> member) {
		this.data.insert(at, member);

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
	public boolean contains(aVector<N> entry) {
		return this.data.contains(entry);
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
	public int length() {
		aVector[] V = this.data.toArray();
		int l = aVectorUtils.longestSize(V);
		return l;
	}

	@Override
	public iGroup resize(int to) {
		// dont use
		return this.data.resize(to);
	}
	
	public aMatrix resize(int size, int length)
	{
		if (size < this.size()) {
			this.truncate(size);
		} else {
			int d = Math.abs(this.size() - size);
			for (int i = 0; i < d; i++) {
				if (this.get(0) != null)
					this.append(new aVector(this.get(0).value).resize(this.length()));
				else
					this.append(new aVector(0).resize(this.length()));
			}
		}

		for (int i = 0; i < this.size(); i++) {
			if (this.get(i) != null)
				this.get(i).resize(length);
		}
		return this;
	}

	@Override
	public void clear() {
		this.data.clear();

	}

	@Override
	public void append(aVector<N> entry) {
		this.data.append(entry);

	}

	@Override
	public void appendAll(aVector<N>... entries) {
		this.data.appendAll(entries);

	}

	@Override
	public void setAt(int at, aVector<N> to) {
		this.data.setAt(at, to);

	}

	@Override
	public void swap(int i, int j) {
		this.data.swap(i, j);
	}

	@Override
	public aVector<N>[] getComponentData() {

		return this.data.getComponentData();
	}

	private static void _COL_() {

	}

	public void appendCol(aVector colVals) {

		if (this.isEmpty())
			for (int i = 0; i < colVals.size(); i++) {
				aVector v = new aVector();
				v.setAt(0, colVals.get(i));
				this.append(v);
			}

		for (int i = 0; i < this.size(); i++) {
			this.get(i).append(colVals.get(i));
		}

	}

	public void appendCol(Number val) {
		for (int i = 0; i < this.size(); i++) {
			this.get(i).append(N_Operator.resolveTo(val, this.get(i).value));
		}
	}

	public void insertCol(int index, aVector colVals) {
		for (int i = 0; i < this.size(); i++) {
			this.get(i).insert(index, colVals.get(i));
		}
	}

	public void insertCol(int index, Number val) {
		for (int i = 0; i < this.size(); i++) {
			this.get(i).insert(index, N_Operator.resolveTo(val, this.get(i).value));
		}
	}

	public void removeCol(int index) {
		for (int i = 0; i < this.size(); i++) {
			aVector at = this.get(i);
			if (index <= at.size())
				at.remove(index);
		}
	}

	public void setCol(int index, aVector v) {

		if (index > this.size()) {
			int d = index - this.size();
			for (int i = 0; i < d; i++) {
				this.appendCol(0);
			}
		}

		for (int i = 0; i < this.size(); i++) {
			if (v.get(i) != null)
				this.get(i).setAt(index, v.get(i));
		}
	}

	public void setCol(int index, Number v) {

		if (index > this.size()) {
			int d = index - this.size();
			for (int i = 0; i < d; i++) {
				this.appendCol(0);
			}
		}

		for (int i = 0; i < this.size(); i++) {
			this.get(i).setAt(index, v);
		}

	}

	public void swapCol(int a, int b) {
		aMatrix M = this.cpy().spinLeft();
		this.swap(a, b);
		this.spinRight();
	}

	private static void _OPS_() {
		// turns this into an identity matrix
	}
	public aMatrix add(Number other)
	{
		return M_Operator.add(this, other);
	}
	
	public aMatrix sub(Number other)
	{
		return M_Operator.sub(this, other);
	}

	public aMatrix mul(Number other)
	{
		return M_Operator.mul(this, other);
	}
	
	public aMatrix div(Number other)
	{
		return M_Operator.div(this, other);
	}
	
	
	////////////
	public aMatrix idt() {
		aMatrix M = this.getIdentity();
		this.set(M);
		return this;
	}
	
	// transpose
	public aMatrix tra() {
		// spin right lol
		// [0] (1,2,3,0) -- (1,4,7);
		// [1] (4,5,6,0) => (2,5,8);
		// [2] (7,8,9,0) -- (3,6,9);
		// {3}------------- (0,0,0);

		aMatrix copy = this.cpy();
		this.clear();

		for (int i = 0; i < copy.length(); i++) {

			aVector newRow = new aVector(N_Operator.resolveTo(0,copy.get(0).get(i)));
			newRow.resize(copy.size());
			this.append(newRow);
		}

		for (int y = 0; y < copy.size(); y++) {
			for (int x = 0; x < copy.length(); x++) {
				this.get(x).setAt(y, copy.get(y).get(x));
			}
		}

		return this;
	}

	// inverse
	public aMatrix inv() {

		// 1/det
		this.toType(0f);
		aMatrix m = this.minor();
		aMatrix a = m.adj();
		float d = this.det();

		return a.mul(1f / d);
	}

	// determinant
	public float det() {

		// need a toArray and toRawArray

		if (!this.isSquare()) {
			int o = this.orthonogality();
			aMatrix M = this.cpy();
			if (o == 1)// taller
			{
				this.appendCol(1);
			}
			if (o == -1)// wider
			{
				aVector tmpRow = new aVector().resize(this.length());
				tmpRow.setAll(1);
				M.append(tmpRow);
			}
			return aMatrixUtils.determinant(M.toRawArray());

		}
		return aMatrixUtils.determinant(this.toRawArray());

		// return 0f;
	}

	// detMat, assigns all values in out matrix M to the determinants of all coords
	// in this matrix
	public aMatrix minor() {
		aMatrix M = this.cpy();
		for (int i = 0; i < this.size(); i++)
			for (int j = 0; j < this.length(); j++) {
				M.set(new aVector(i, j), this.getMinor(i, j).det());
			}

		return M;
	}

	public aMatrix getMinor(int at) {
		return this.getMinor(0, at);
	}

	public aMatrix getMinor(int x, int y) {
		// Log(" >> " + x + "," + y);

		aMatrix M = this.cpy();

		// Log(M.toLog());
		M.remove(x);
		// Log(M.toLog());
		M.removeCol(y);
		// Log(M.toLog());
		return M;

	}

	public aMatrix cof() {
		return getCofactors();
	}

	public aMatrix getCofactors() {
		aMatrix M = new aMatrix(this.size(), this.length());
		for (int i = 0; i < this.size(); i++)
			for (int j = 0; j < this.length(); j++) {
				int tgt = 1;
				if (Maths.isOdd(i + j))
					tgt = -1;

				M.set(new aVector(i, j), tgt);
			}
		return M;
	}

	public aMatrix adj() {
		return this.adjoint();
	}

	public aMatrix adjoint() {// diagonal swap
		// [0] (1,2,3) -- (1,4,7)
		// [1] (4,5,6) => (2,5,8)
		// [2] (7,8,9) -- (3,6,9)

		aMatrix M = this.cpy();

		this.clear();
		for (int i = 0; i < M.size(); i++)
			this.appendCol(M.get(i));

		return this;
	}

	private static void _UTIL_() {

	}
	
	public boolean isSquare() {
		return this.size() == this.length();
	}

	public aMatrix square(int size) {
		return this.resize(size, size);
	}

	public int orthonogality() {
		// taller than wider
		if (this.size() > this.length())
			return 1;

		// wider than taller
		if (this.size() < this.length())
			return -1;

		return 0;
	}

	public aMatrix spinLeft() {
		// [0] (1,2,3) -- (3,6,9)
		// [1] (4,5,6) => (2,5,8)
		// [2] (7,8,9) -- (1,5,7)

		aMatrix M = this.cpy();

		this.clear();
		for (int i = 0; i < M.size(); i++)
			this.appendCol(M.get(i).inv());

		return this;
	}

	public aMatrix spinRight() {
		// [0] (1,2,3) -- (7,4,1)
		// [1] (4,5,6) => (8,5,2)
		// [2] (7,8,9) -- (9,6,3)

		aMatrix M = this.cpy();

		this.clear();
		for (int i = M.size() - 1; i >= 0; i--)
			this.appendCol(M.get(i));

		return this;
	}

	public aMatrix truncate(int to) {
		for (int i = to; i <= this.size(); i++) {
			this.remove(to);
		}
		return this;
	}

	public aMatrix truncate(int size, int length) {
		for (int i = size; i <= this.size(); i++) {
			this.remove(size);
		}
		for (int i = size; i <= this.length(); i++) {
			this.get(i).truncate(length);
		}
		return this;
	}

	// gets without modifying the original;
	public aMatrix getIdentity() {
		aMatrix M = this.cpy();
		for (int i = 0; i < this.size(); i++)
			for (int j = 0; j < this.size(); j++)
				if (i == j)
					M.set(1, new aVector<Integer>(i, j));
				else
					M.set(0, new aVector<Integer>(i, j));

		return M;
	}

	@Override
	public short shortValue() {
		return (short) this.size();
	}

	@Override
	public int intValue() {

		return this.size() * this.length();
	}

	@Override
	public long longValue() {

		return this.length();
	}

	@Override
	public float floatValue() {

		return this.size() / this.length();
	}

	@Override
	public double doubleValue() {
		aVector[] V = this.data.toArray();
		return V_Operator.sum(V).doubleValue();
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

	public aMatrix toType(Number n) {
		for (int i = 0; i < this.size(); i++)
			this.get(i).toType(n);

		return this;
	}

	@Override
	public aVector<N>[] toArray() {
		return this.data.toArray();
	}
	
	public _Map.Entry<aVector, aVector> toRaw()
	{
		aVector out =  new aVector();
		for(aVector v : this.data)
			out.appendAll(out.toArray());
		return new _Map.Entry<aVector, aVector>(new aVector(this.size(),this.length()), out);
	}
	
	public Number[][] toRawArray() {
		Number[][] result = new Number[this.size()][this.length()];
		for (int i = 0; i <= this.size() - 1; i++)
			for (int j = 0; j <= this.length() - 1; j++)
				result[i][j] = this.getAt(new aVector(i, j));

		return result;
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

	@Override
	public Integer getIndexType() {
		return 0;
	}

}
