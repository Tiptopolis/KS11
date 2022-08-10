package com.uchump.prime.Core.Primitive.Struct.Impl;

import static com.uchump.prime.Core.uAppUtils.*;

import com.uchump.prime.Core.Primitive.iFunctor;
import com.uchump.prime.Core.Primitive.Struct.aMap;

public class aCommandMap extends aMap<String, iFunctor> {

	public Object call(String name, Object... args) {
		iFunctor F = this.get(name);

		if (args == null || args.length == 0)
			return this.call(name, "");

		else
			return F.apply(args);

	}

	public Object callLog(String name, Object... args) {
		iFunctor F = this.get(name);
		Log("@:> " + F + " => " + args);
		Object o = this.call(name, args);
		Log(">>" + o);
		return o;
	}

}
