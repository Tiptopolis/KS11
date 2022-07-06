package com.uchump.prime.Core.System;

import com.uchump.prime.Core.Primitive.Struct.Impl.aQueue;
import com.uchump.prime.Core.System.Event.iEvent;

public interface iTimeFrame {
//Read-Eval-Print Loop
	
	public void update(Number delta);
	
	public default void update() {
		this.update(1);
	}
	
	/*public default aQueue<iEvent> getPending()
	{
		
	}*/
}
