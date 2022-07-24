package com.uchump.prime.Core.System.Event;

public interface iHandler<T> {

	public boolean handle(T o);
	

	public default boolean handle(String o)
	{
		return false;
	}
	
	public default boolean isActive()
	{
		return true;
	}
	

}
