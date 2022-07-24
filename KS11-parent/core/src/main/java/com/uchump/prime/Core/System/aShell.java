package com.uchump.prime.Core.System;

import com.uchump.prime.Core.Primitive.aGroup;
import com.uchump.prime.Core.Primitive.Struct._Map;
import com.uchump.prime.Core.Primitive.Struct.aDictionary;
import com.uchump.prime.Core.System.Environment.aEnvironment;

public class aShell extends aEnvironment{

	//node local-addresses are mapped by radial distance from central node
	//Every node has/is aTransform
	public _Map<Number,aDictionary> dependencies;
	
}
