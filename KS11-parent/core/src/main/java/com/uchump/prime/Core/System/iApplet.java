package com.uchump.prime.Core.System;

import static com.uchump.prime.Core.uAppUtils.*;


public interface iApplet {

	public void init();
	public default void update(Number delta)
	{
		if(instanceOf(Float.class,Double.class).test(delta))
			this.update(delta.floatValue());
		else
			this.update(delta.intValue());
	}
	public void update(int delta);
	public void update(float delta);
	public void exit();
	
}
