package com.uchump.prime.Core.System;

import java.io.IOException;

import com.uchump.prime.Core.Primitive.A_I.iCycle;
import com.uchump.prime.Core.Primitive.Struct.Impl.aQueue;
import com.uchump.prime.Core.System.Event.iEvent;

public interface iTimeFrame extends iCycle{
//Read-Eval-Print Loop
	
	public default void update(Number delta) {
		try {
			this.next();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public default void update() {
		this.update(1);
	}
	
}
