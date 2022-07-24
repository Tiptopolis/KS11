package com.uchump.prime.Core.System.Environment;

import java.io.IOException;

import com.uchump.prime.Core.Primitive.A_I.iCycle;
import com.uchump.prime.Core.System.aShell;
import com.uchump.prime.Core.System.iTimeFrame;

public class _RuntimeEnv extends aShell implements iTimeFrame{

	@Override
	public void next() throws IOException {
		
		
	}
	
	@Override
	public String toToken() {
		String tag = "";
		tag = this.getClass().getSimpleName();
		return "<" + tag + ">";
	}
	

}
