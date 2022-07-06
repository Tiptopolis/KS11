package com.uchump.prime.Core.Primitive.Utils;

import com.uchump.prime.Core.Primitive.aNode;
import com.uchump.prime.Core.Primitive.Struct.aMap;

public class aThingIndexer<T> {

	//so, iterates thru array/collection of T & appends the indices of their value
	public aMap<T, aNode<Integer>> things;
	
	public aThingIndexer(T...things)
	{
		this.things = new aMap<T, aNode<Integer>>();
		for(int i =0; i < things.length; i++)
		{
			this.things.put(things[i],i);
		}			
	}
	
	public int count(T key)
	{
		return this.things.getAll(key).size();
	}
	
}

