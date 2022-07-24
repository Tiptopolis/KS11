package com.uchump.prime.Core.Primitive.Struct;

import static com.uchump.prime.Core.uAppUtils.*;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.uchump.prime.Core.Primitive.aNode;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iGroup;
import com.uchump.prime.Core.Primitive.A_I.iMap;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Request.Content.Consumer;

public class aMap<K, V> extends _Map<K, V> {

	//was aSetMap previously
	// index-mapped lists
	// 1-1 mapping only

	@Override
	public void put(K key, Object val) {

		if (!this.containsKey(key)) {
			this.keys.append(key);
			this.values.append((V) val);
		} else {
			this.set(key, (V) val);
		}

	}

	public void put(K key, Object... vals) {
		if(vals!=null)
		for (Object v : vals) {
			this.put(key, v);
		}
	}

	@Override
	public void set(K key, V val) {
		// setAll
		if (!this.containsKey(key))
			this.put(key, val);
		int i = this.keys.indexOf(key);
		this.values.setAt(i, val);

	}
	
	@Override
	public  iMap<K, V> toMap() {
		return this;
	}

	@Override
	public Integer getIndexType() {
		
		return 0;
	}

	
	@Override
	public String toLog()
	{
		String log = this.getClass().getSimpleName()+"\n";
		for(Entry<K,V> E : this)
		{
			K key = E.getKey();
			V val = E.getValue();
			if(val instanceof Supplier)			
				log += key + ":() -> "+((Supplier)val).get()+"\n";
			else if(val instanceof Predicate)
				log += key + ":(!?)"+((Predicate)val).test(this);
			else if(val instanceof Function )
				log += key + ":(?)->(!)\n";
			else if(val instanceof BiFunction)
				log += ":(?,?)->(!)\n";			
			else if(val instanceof aNode)
			log += ((aNode)val).toToken()+"\n";
			else 
				log += key + " | " + val+"\n";
			
		}
		
		
		return log;
	}
	
}
