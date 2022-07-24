package com.uchump.prime.Core.Primitive.Struct.Graph.Document;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.uchump.prime.Core.Primitive.aNode;
import com.uchump.prime.Core.Primitive.Struct.Graph.aBranch;

public class aDocument extends aBranch{

	
	 protected static final String[] NODE_TYPE_NAMES = {"Node", "Element",
	            "Attribute", "Text", "CDATA", "Entity", "Entity",
	            "ProcessingInstruction", "Comment", "Document", "DocumentType",
	            "DocumentFragment", "Notation", "Namespace", "Unknown" };
	 

	 public aDocument()
	 {

	 }
	 
	public static aNode<String> newDomNode()
	{
		aNode<String> N = new aNode();
		
		N.set("parent", aNode.NULL);
		
		/*Function<aNode,aNode> setParent = (a)->{N.set("parent", a); return N;};
		Supplier<aNode> getParent = ()->(aNode)N.get("parent");
		Predicate<Supplier<aNode>> _hasParent = s->s.get()!=null;
		Supplier<Boolean> hasParent = ()->(_hasParent.test(getParent));
		N.shared.put("getParent", getParent);
		N.shared.put("setParent", setParent);
		N.shared.put("hasParent", hasParent);*/
		
		return N;
	}
}
