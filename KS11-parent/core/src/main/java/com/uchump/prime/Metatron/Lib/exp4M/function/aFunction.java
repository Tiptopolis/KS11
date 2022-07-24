package com.uchump.prime.Metatron.Lib.exp4M.function;

import com.uchump.prime.Core.Primitive.iFunctor;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Utils.StringUtils;

public abstract class aFunction<T> implements iFunctor.Effect<T> {

	protected String name = "";
	protected int numArguements = 0;

	public aFunction(String name, int nArgs) {
		this.name = name;
		this.numArguements = nArgs;
	}

	public aFunction(String name) {
		this.name = name;
		this.numArguements = 1;
	}

	

	public String getName() {
		return this.name;
	}

	public int getNumArgs() {
		return this.numArguements;
	}
	public boolean is(Object other) {
		if (other == this)
			return true;
		if (other instanceof aFunction) {
			aFunction F = (aFunction) other;
			if (F.name.equals(this.name) && F.numArguements==this.numArguements)
				return true;
		}
		return false;
	}
	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;
		if (other instanceof aFunction) {
			aFunction F = (aFunction) other;
			if (F.name.equals(this.name))
				return true;
		}
		if(other instanceof CharSequence)
		{
			String o = ""+other;
			if (StringUtils.isFormOf(this.name, o))
				return true;
		}
		return false;
	}



}
