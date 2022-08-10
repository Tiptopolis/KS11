package com.uchump.prime.Core.Primitive.Struct.Graph;

import com.uchump.prime.Core.Primitive.aLink;
import com.uchump.prime.Core.Primitive.aNode;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.A_I.iGraph;
import com.uchump.prime.Core.Primitive.A_I.iMap;
import com.uchump.prime.Core.Primitive.A_I.iNode;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Primitive.Struct.aMultiMap;
import com.uchump.prime.Core.Primitive.Struct.aSet;
import com.uchump.prime.Core.Primitive.Struct.Impl.aRegistry;

public class aGraph extends aNode implements iGraph{

	iNode root;
	public aSet<iNode> members;
	public aRegistry<iNode> edges;
	public aSet<String> connectionTypes;
	
	
	public aGraph()
	{
		super();
		this.root = this;
		this.members = new aSet<iNode>();
		this.edges = new aRegistry<iNode>();
		this.connectionTypes = new aSet<String>();
	}
	
	
	public aList<aMultiMap.Entry> getLinks() {

		aList<aMultiMap.Entry> out = new aList<aMultiMap.Entry>();
		for (Object o : this.members)
			out.append((aMultiMap.Entry) o);

		return out;
	}
	
	@Override
	public iCollection getMembers() {
		return this.members;
	}
	@Override
	public iMap getEdges() {
		return this.edges;
	}
}
