package com.uchump.prime.Core.Math.Primitive.BAK;

import static com.uchump.prime.Core.Utils.StringUtils.commaLen;
import static com.uchump.prime.Core.uAppUtils.Log;

import com.badlogic.gdx.math.Matrix4;
import com.uchump.prime.Core.Math.N_Operator;
import com.uchump.prime.Core.Math.Primitive.A_I.iMatrix;
import com.uchump.prime.Core.Math.Utils.Maths;
import com.uchump.prime.Core.Math.Utils.aMatrixUtils;
import com.uchump.prime.Core.Math.Utils.aVectorUtils;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.Struct.aList;

public class aMatrix  extends aList<aVector> implements iMatrix{
	// Matrix3 d;
		// Matrix4 m;
		// Camera c;

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

		public static aMatrix aIdentity(int size) {
			return new aMatrix(size).idt();
		}

		public static aMatrix aTranslation(aVector dst) {
			int dim = dst.size();
			aMatrix M = new aMatrix(dim + 1).idt();
			M.get(dim).add(dst);

			return M;
		}

		public void set(int index, aVector val) {
			this.get(index).set(val);

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

		public void fill(int index, Number n) {
			this.get(index).setAll(n);
		}

		public void fillCol(int index, Number n) {
			for (aVector V : this.data)
				V.setAt(index, n);
		}

		public void fill(Number n) {
			for (aVector v : this.toArray())
				v.setAll(n);
		}

		public Number getAt(aVector address) {

			//
			int mX = 0;
			int mY = address.get(1).intValue();
			if (address.get(0) != null)
				mX = address.get(0).intValue();
			if (address.get(1) != null)
				mY = address.get(1).intValue();

			aVector V = this.get(mX);

			// Log(":: " + address);
			// Log(":: " + V);

			return V.get(mY);
		}

		public aMatrix toType(Number n) {
			for (int i = 0; i < this.size(); i++)
				this.get(i).toType(n);

			return this;
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
				this.get(i).append(N_Operator.resolveTo(val,this.get(i).value));
			}
		}

		public void insertCol(int index, aVector colVals) {
			for (int i = 0; i < this.size(); i++) {
				this.get(i).insert(index, colVals.get(i));
			}
		}

		public void insertCol(int index, Number val) {
			for (int i = 0; i < this.size(); i++) {
				this.get(i).insert(index, N_Operator.resolveTo(val,this.get(i).value));
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

		// width of matrix based on longest vector
		public int length() {
			int s = 0;
			for (aVector v : this)
				if (v.size() > s)
					s = v.size();
			return s;
		}

		///////// OPS

		public aMatrix add(aMatrix m) {
			int s = Math.min(this.size(), m.size());
			int len = Math.min(this.length(), m.length());
			for (int i = 0; i < s; i++) {
				aVector I = m.get(i);
				// aVector def = new aVector().fill(len,0);

				this.get(i).add(m.get(i));
			}

			return this;
		}

		// assumes 0 for missing values
		public aMatrix add(aVector v) {
			for (int i = 0; i < this.size(); i++) {

				this.get(i).add(v);
			}

			return this;
		}

		public aMatrix add(Number n) {
			for (int i = 0; i < this.size(); i++) {

				this.get(i).add(n);
			}

			return this;
		}

		public aMatrix sub(aMatrix m) {
			int s = Math.min(this.size(), m.size());
			int len = Math.min(this.length(), m.length());
			for (int i = 0; i < s; i++) {
				aVector I = m.get(i);
				// aVector def = new aVector().fill(len,0);

				this.get(i).sub(m.get(i));
			}

			return this;
		}

		// assumes 0 for missing values
		public aMatrix sub(aVector v) {
			for (int i = 0; i < this.size(); i++) {

				this.get(i).sub(v);
			}

			return this;
		}

		public aMatrix sub(Number n) {
			for (int i = 0; i < this.size(); i++) {

				this.get(i).sub(n);
			}

			return this;
		}

		public aMatrix mul(aMatrix m) {
			int s = Math.min(this.size(), m.size());
			int len = Math.min(this.length(), m.length());
			for (int i = 0; i < s; i++) {
				aVector I = m.get(i);
				// aVector def = new aVector().fill(len,0);

				this.get(i).mul(m.get(i));
			}

			return this;
		}

		// assumes 0 for missing values
		public aMatrix mul(aVector v) {
			for (int i = 0; i < this.size(); i++) {

				this.get(i).mul(v);
			}

			return this;
		}

		public aMatrix mul(Number n) {
			for (int i = 0; i < this.size(); i++) {

				this.get(i).mul(n);
			}

			return this;
		}

		public aMatrix div(aMatrix m) {
			int s = Math.min(this.size(), m.size());
			int len = Math.min(this.length(), m.length());
			for (int i = 0; i < s; i++) {
				aVector I = m.get(i);
				// aVector def = new aVector().fill(len,0);

				this.get(i).div(m.get(i));
			}

			return this;
		}

		// assumes 0 for missing values
		public aMatrix div(aVector v) {
			for (int i = 0; i < this.size(); i++) {

				this.get(i).div(v);
			}

			return this;
		}

		public aMatrix div(Number n) {
			for (int i = 0; i < this.size(); i++) {

				this.get(i).div(n);
			}

			return this;
		}

		///////// ADV OPS

		// turns this into an identity matrix
		public aMatrix idt() {
			aMatrix M = this.getIdentity();
			this.set(M);
			return this;
		}

		// gets without modifying the origina;
		public aMatrix getIdentity() {
			aMatrix M = this.cpy();
			for (int i = 0; i < this.size(); i++)
				for (int j = 0; j < this.size(); j++)
					if (i == j)
						M.set(new aVector<Integer>(i, j), 1);
					else
						M.set(new aVector<Integer>(i, j), 0);

			return M;
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

		public aMatrix setToLookAt(aVector dir, aVector up) {
			aVector l_z = dir.cpy().nor();
			aVector l_x = dir.cpy().crs(up.cpy()).nor();
			aVector l_y = l_x.cpy().crs(l_z).nor();

			this.idt();
			this.append(l_x);
			this.append(l_y);
			this.append(l_z.mul(-1));

			return this;
		}

		public aMatrix setToProjection(float near, float far, float fovy, float aspectRatio) {
			this.idt();
			float l_fd = (float) (1.0 / Math.tan((fovy * (Math.PI / 180)) / 2.0));
			float l_a1 = (far + near) / (near - far);
			float l_a2 = (2 * far * near) / (near - far);

			// columns
			aVector mc1 = new aVector(l_fd / aspectRatio, 0, 0, 0);
			aVector mc2 = new aVector(0f, l_fd, 0, 0);
			aVector mc3 = new aVector(0f, 0, l_a1, -1);
			aVector mc4 = new aVector(0f, 0, l_a2, 0);

			this.setCol(0, mc1);
			this.setCol(1, mc2);
			this.setCol(2, mc3);
			this.setCol(3, mc4);

			return this;
		}
		
		public aMatrix setToProjection(float left, float right, float bottom, float top, float near, float far) {
			
			
			
			return this;
		}

		/////////

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

		public aMatrix toSize(int to) {

			return this.toSize(to, to);
		}

		// size is 'height', length is 'width'
		public aMatrix toSize(int size, int length) {
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

		// (y,x)
		public aMatrix expandAt(int r, int c) {
			// plot (1)@(r,c) and (0->r)(0->c)
			// insert new row,col@(x,y),

			// EX PUSH PULL
			// [a,a] [0,0,0] [a,a,0] [a,0,a]
			// [b,b] [0,a,a] [b,b,0] [0,0,0]
			// [0,b,b] [0,0,0] [b,0,b]

			return this;
		}

		public boolean isSquare() {
			return this.size() == this.length();
		}

		public aMatrix square(int size) {
			return this.toSize(size, size);
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

		public aMatrix stamp(int offX, int offY, aMatrix values) {

			// (32,32) -:2,2:- (40,40) - overlap area is(30x30)
			// output is (42,42)
			int tW = this.length();
			int tH = this.size();
			int oW = values.length();
			int oH = values.size();

			int W = tW;
			int H = tH;
			if (oW > (tW - offX)) // if 40 >32-2
				W = tW + (oW - (tW - offX)); // W = 32+(40-(32-2));->32+40-30;->42

			if (oH > (tH - offY))
				H = tH + (oH - (tH - offY));

			aMatrix M = new aMatrix().toSize(W, H);
			Log(M.toLog());

			int dirX = N_Operator.sign(offX).intValue();
			int dirY = N_Operator.sign(offY).intValue();
			for (int x = 0; x < M.length() - 1; x++)
				for (int y = 0; y < M.size() - 1; y++) {
					int X = x;
					int Y = y;
					aMatrix from = this;

					/*
					 * if (x >= offX) { X=x-offX; from = values; //Log(x+"x>" + X); }
					 * 
					 * if (y >= offY) { Y = y-offY; from = values; //Log(y+"y>" + Y); }
					 */
					if (x >= offX && y >= offY) {
						X = x - offX;
						Y = y - offY;
						from = values;
					}

					aVector<Integer> address = new aVector<Integer>(X, Y);
					// Log(address + " " + from.getAt(address));
					Number n = from.getAt(address);
					M.set(new aVector(x, y), from.getAt(address));
				}

			// cheats lol
			for (int i = 0; i <= offY; i++)
				M.remove(M.size() - 1);

			this.set(M);
			return this;
		}

		public aMatrix cpy() {
			aMatrix copy = new aMatrix();
			for (int i = 0; i < this.size(); i++) {
				copy.append(this.get(i).cpy());
			}

			return copy;
		}

		@Override
		public aVector[] toArray() {
			aVector[] V = new aVector[this.size()];
			for (int i = 0; i < this.size(); i++)
				V[i] = this.get(i);

			return V;
		}

		public Number[][] toRawArray() {
			Number[][] result = new Number[this.size()][this.length()];
			for (int i = 0; i <= this.size() - 1; i++)
				for (int j = 0; j <= this.length() - 1; j++)
					result[i][j] = this.getAt(new aVector(i, j));

			return result;
		}

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
		public String toLog() {
			String log = this.getClass().getSimpleName() + "{" + this.size() + "x" + this.length() + "}\n";
			if (this.data != null)
				for (int i = 0; i < this.size(); i++) {
					log += "[" + i + "]" + this.get(i) + "\n";
				}
			return log;
		}
}
