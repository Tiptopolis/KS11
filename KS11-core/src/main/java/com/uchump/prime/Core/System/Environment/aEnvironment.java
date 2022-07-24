package com.uchump.prime.Core.System.Environment;

import com.uchump.prime.Core.Primitive.aGroup;
import com.uchump.prime.Core.Primitive.A_I.iGroup;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Primitive.Struct.aSet;
import com.uchump.prime.Core.System.Event.iEvent;
import com.uchump.prime.Core.System.Event.iEventHandler;
import com.uchump.prime.Core.System.Event.iHandler;

public class aEnvironment<N extends Number,T> extends aGroup<N,T> implements iEventHandler{

	//public aSet<T> members = new aSet<T>();


	
	
	@Override
	public boolean handle(iEvent o) {
		return false;
	}
	
	@Override
	public String toToken() {
		String tag = "";
		tag = this.getClass().getSimpleName();
		return "<" + tag + ">";
	}
	
}
