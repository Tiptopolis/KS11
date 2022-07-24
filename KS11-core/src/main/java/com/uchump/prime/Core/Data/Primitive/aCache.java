package com.uchump.prime.Core.Data.Primitive;

import java.lang.ref.WeakReference;
import java.util.function.Function;

import com.uchump.prime.Core.Primitive.iFunctor.Effect;
import com.uchump.prime.Core.Data.Primitive.A_I.iCRUD;
import com.uchump.prime.Core.Primitive.A_I.iMap;
import com.uchump.prime.Core.Primitive.Struct.aMap;

public class aCache<T> extends aMap<String, iMap<String, WeakReference<T>>> implements iCRUD<Integer,T>{
	protected aMap<String, iMap<String, WeakReference<Object>>> cache;
	protected aMap<String, WeakReference<Object>> noPrefixCache;
	
	
	@Override
	public T create(aMap<String, T> params) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public T create(Object entry) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public T create(Object... entries) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public <X> T create(Function<X, T> f) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public T create(Effect<T> f) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public T read(Integer indx) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public T[] read(Integer from, Integer to) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void update(Integer index, T entry) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void update(Integer index, Effect<T> f) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void delete(Integer index) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void delete(Integer from, Integer to) {
		// TODO Auto-generated method stub
		
	}
	


	
	/*public T get(String prefix, String uri)
	{
		return null;
	}
	public T get(String uri)
	{
		return null;
	}
	@Override
	public void create(T entry) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void create(T... entries) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void create(aMap<String, T> params) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public <X> void create(Function<X, T> f) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void create(Effect<T> f) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public T read(Integer indx) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public T[] read(Integer from, Integer to) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void update(Integer index, T entry) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void update(Integer index, Effect<T> f) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void delete(Integer index) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void delete(Integer from, Integer to) {
		// TODO Auto-generated method stub
		
	}*/
	
	
}
