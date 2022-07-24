package com.uchump.prime.Core.Primitive.Struct.Graph;

import java.lang.ref.WeakReference;

import com.uchump.prime.Core.Data.Primitive.aCache;
import com.uchump.prime.Core.Primitive.A_I.iMap;
import com.uchump.prime.Core.Primitive.Struct.aMap;

public class NamespaceCache extends aCache<aNamespace>{
	protected static aMap<String, iMap<String, WeakReference<aNamespace>>> staticCache;
	protected static aMap<String, WeakReference<aNamespace>> staticNoPrefixCache;
	
	 static {
		 staticCache = new aMap<String, iMap<String, WeakReference<aNamespace>>>();
		 staticNoPrefixCache = new aMap<String, WeakReference<aNamespace>>();
	    }
}
