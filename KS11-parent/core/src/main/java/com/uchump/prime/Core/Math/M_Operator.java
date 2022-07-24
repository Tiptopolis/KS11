package com.uchump.prime.Core.Math;

import com.uchump.prime.Core.Math.Primitive.aMatrix;
import com.uchump.prime.Core.Math.Primitive.aVector;

public abstract class M_Operator {

	public static aMatrix Identity(int size) {
		aMatrix M = new aMatrix(size);
		for (int i = 0; i < M.size(); i++)
			for (int j = 0; j < M.size(); j++)
				if (i == j)
					M.set(1, new aVector<Integer>(i, j));
				else
					M.set(0, new aVector<Integer>(i, j));

		return M;
	}

	public static float determinant(Number[][] matrix) {
		// Log(matrix);
		float[][] r = new float[matrix[0].length][matrix[1].length];
		for (int i = 0; i < matrix[0].length; i++)
			for (int j = 0; j < matrix[1].length; j++) {
				r[i][j] = matrix[i][j].floatValue();
			}

		return determinant(r);
	}

	public static float determinant(float[][] matrix) { // method sig. takes a matrix (two dimensional array), returns
														// determinant.
		float sum = 0;
		float s;
		if (matrix.length == 1) { // bottom case of recursion. size 1 matrix determinant is itself.
			return (matrix[0][0]);
		}
		for (int i = 0; i < matrix.length; i++) { // finds determinant using row-by-row expansion
			float[][] smaller = new float[matrix.length - 1][matrix.length - 1]; // creates smaller matrix- values not
																					// in same row, column
			for (int a = 1; a < matrix.length; a++) {
				for (int b = 0; b < matrix.length; b++) {
					if (b < i) {
						smaller[a - 1][b] = matrix[a][b];
					} else if (b > i) {
						smaller[a - 1][b - 1] = matrix[a][b];
					}
				}
			}
			if (i % 2 == 0) { // sign changes based on i
				s = 1;
			} else {
				s = -1;
			}
			sum += s * matrix[0][i] * (determinant(smaller)); // recursive step: determinant of larger determined by
																// smaller.
		}
		return (sum); // returns determinant value. once stack is finished, returns final determinant.
	}

	////[ADD]->(a+b)
	public static aMatrix add(aMatrix M, Number n) {
		for (aVector r : M.getComponentData())
			r.add(n);

		return M;
	}

	public static aMatrix add(aMatrix M, aVector v) {
		for (aVector r : M.getComponentData())
			r.add(v);

		return M;
	}

	public static aMatrix add(aMatrix M, aMatrix N) {

		int l = Math.min(M.size(), N.size());

		for (int i = 0; i < l; i++)
			M.setAt(i, M.get(i).add(N.get(i)));

		return M;
	}
	
	////[SUB]->(a-b)
	public static aMatrix sub(aMatrix M, Number n) {
		for (aVector r : M.getComponentData())
			r.sub(n);

		return M;
	}

	public static aMatrix sub(aMatrix M, aVector v) {
		for (aVector r : M.getComponentData())
			r.sub(v);

		return M;
	}

	public static aMatrix sub(aMatrix M, aMatrix N) {

		int l = Math.min(M.size(), N.size());

		for (int i = 0; i < l; i++)
			M.setAt(i, M.get(i).sub(N.get(i)));

		return M;
	}
	
	////[MUL]->(a*b)
	public static aMatrix mul(aMatrix M, Number n) {
		for (aVector r : M.getComponentData())
			r.mul(n);

		return M;
	}

	public static aMatrix mul(aMatrix M, aVector v) {
		for (aVector r : M.getComponentData())
			r.mul(v);

		return M;
	}

	public static aMatrix mul(aMatrix M, aMatrix N) {

		int l = Math.min(M.size(), N.size());

		for (int i = 0; i < l; i++)
			M.setAt(i, M.get(i).mul(N.get(i)));

		return M;
	}
	
	////[DIV]->(a/b)
	public static aMatrix div(aMatrix M, Number n) {
		for (aVector r : M.getComponentData())
			r.mul(n);

		return M;
	}

	public static aMatrix div(aMatrix M, aVector v) {
		for (aVector r : M.getComponentData())
			r.mul(v);

		return M;
	}

	public static aMatrix div(aMatrix M, aMatrix N) {

		int l = Math.min(M.size(), N.size());

		for (int i = 0; i < l; i++)
			M.setAt(i, M.get(i).mul(N.get(i)));

		return M;
	}
}
