package com.uchump.prime.Core.Primitive.Utils;

import static com.uchump.prime.Core.uAppUtils.*;

import com.uchump.prime.Core.Math.Primitive.aVector;
import com.uchump.prime.Core.Math.Utils.aVectorUtils;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Primitive.Struct.aSet;


public abstract class aThingSampler<T> extends aList<T> {

	public aThingSampler() {
		super();
		
	}

	public aThingSampler(iCollection<T> things) {
		super(things);
		
	}

	public iCollection sample(iCollection<T> things) {
		return null;
	}

	public static class Regular<T> extends aThingSampler<T> {
		private int freq = 1;

		public Regular(iCollection<T> things, int Nth) {
			//super();
			this.freq = Nth;
			this.sample(things);
			
		}

		@Override
		public iCollection<T> sample(iCollection<T> things) {
			if (!this.isEmpty())
				this.clear();

			int mod = (things.size() % this.freq);
			for (int i = 0; i <= things.size() - mod; i += freq) {
				this.append(things.get(i));
			}
			return this;
		}
	}
	
	public static class Random<T> extends aThingSampler<T>
	{
		private int number = 1;
		private float freq = 1f;
		
		public Random(iCollection<T> things, int num)
		{
			if(num > things.size())
				num = things.size();
			this.number = num;
			
			
			this.sample(things);
		}
		
		@Override
		public iCollection<T> sample(iCollection<T> things)
		{
			//random dissolution of index
			
			int s = things.size();
			aVector<Integer> index = new aVectorUtils().index(s);
			aSet<Integer> indices = new aSet<Integer>();
			
 			while(indices.size()<this.number)
			{
 				s = things.size();
				java.util.Random r = new java.util.Random();
				int at = r.nextInt(s);
				indices.append(at);
				
			}
 			
			for(int i =0; i < indices.size(); i++)
 			{
 				int at = indices.get(i);
 				this.append(things.get(indices.get(i)));
 			}
			
			return this;
		}
	}
}
