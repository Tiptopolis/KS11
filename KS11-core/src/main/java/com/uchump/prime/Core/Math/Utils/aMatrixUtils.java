package com.uchump.prime.Core.Math.Utils;

import static com.uchump.prime.Core.uAppUtils.*;



public class aMatrixUtils {

	
	
	public static float determinant(Number[][] matrix)
	{
		//Log(matrix);
		float[][] r = new float[matrix[0].length][matrix[1].length];
		for(int i=0; i < matrix[0].length; i++)
			for(int j = 0; j < matrix[1].length; j++) {				
				r[i][j] = matrix[i][j].floatValue();
			}
		
		
		return determinant(r);
	}
	
	public static float determinant(float[][] matrix){ //method sig. takes a matrix (two dimensional array), returns determinant.
		float sum=0; 
		float s;
	    if(matrix.length==1){  //bottom case of recursion. size 1 matrix determinant is itself.
	      return(matrix[0][0]);
	    }
	    for(int i=0;i<matrix.length;i++){ //finds determinant using row-by-row expansion
	    	float[][]smaller= new float[matrix.length-1][matrix.length-1]; //creates smaller matrix- values not in same row, column
	      for(int a=1;a<matrix.length;a++){
	        for(int b=0;b<matrix.length;b++){
	          if(b<i){
	            smaller[a-1][b]=matrix[a][b];
	          }
	          else if(b>i){
	            smaller[a-1][b-1]=matrix[a][b];
	          }
	        }
	      }
	      if(i%2==0){ //sign changes based on i
	        s=1;
	      }
	      else{
	        s=-1;
	      }
	      sum+=s*matrix[0][i]*(determinant(smaller)); //recursive step: determinant of larger determined by smaller.
	    }
	    return(sum); //returns determinant value. once stack is finished, returns final determinant.
	  }
	
	
	
	
	public static float determinantX(Number[][] matrix)
	{
		float[][] r = new float[matrix[0].length][matrix[1].length];
		for(int i=0; i < matrix.length; i++)
			for(int j = 0; j < matrix[0].length; j++)
				r[i][j] = matrix[i][j].floatValue();
		
		
		return determinant(r);
	}
	
	//assumes square, missing dimensions are assumed 1
	public static float determinantX(float[][] matrix){ //method sig. takes a matrix (two dimensional array), returns determinant.
		float sum=0; 
		float s;
	    if(matrix.length==1){  //bottom case of recursion. size 1 matrix determinant is itself.
	      return(matrix[0][0]);
	    }
	    for(int i=0;i<matrix.length;i++){ //finds determinant using row-by-row expansion
	    	float[][]smaller= new float[matrix.length-1][matrix[0].length-1]; //creates smaller matrix- values not in same row, column
	      for(int a=1;a<matrix.length;a++){
	        for(int b=0;b<matrix[0].length;b++){
	          if(b<i){
	            smaller[a-1][b]=matrix[a][b];
	          }
	          else if(b>i){
	            smaller[a-1][b-1]=matrix[a][b];
	          }
	        }
	      }
	      if(i%2==0){ //sign changes based on i
	        s=1;
	      }
	      else{
	        s=-1;
	      }
	      sum+=s*matrix[0][i]*(determinant(smaller)); //recursive step: determinant of larger determined by smaller.
	    }
	    return(sum); //returns determinant value. once stack is finished, returns final determinant.
	  }
	

	
}
