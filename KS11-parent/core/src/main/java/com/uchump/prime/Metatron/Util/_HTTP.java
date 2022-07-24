package com.uchump.prime.Metatron.Util;

import javax.script.*;

import com.uchump.prime.Core.Primitive.iFunctor.Consumer;
import com.uchump.prime.Core.Primitive.iFunctor.Supplier;

import com.sun.net.httpserver.HttpExchange;


public interface _HTTP<URI extends HttpExchange> {
	
	public<O> O Get(URI uri); //retrieve information from the given server using a given URI
	public<O> O Head(URI uri);
	public<O> O Put(URI uri); 
	public<O> O Post(URI uri);
	public<O> O Delete(URI uri);
	public<O> O Connect(URI uri);
	public<O> O Options(URI uri);
	public<O> O Trace(URI uri);

}
