package com.uchump.prime.Core.Data.Imp.DOM;

import java.util.function.Supplier;

import com.uchump.prime.Core.Primitive.aNode;
import com.uchump.prime.Core.Primitive.Struct.aMap;
import com.uchump.prime.Metatron.Lib._DOM.core.Element;
import com.uchump.prime.Metatron.Lib._DOM.core.Node;

public class Dom_Imp {
	//public aAtlas<
	public static aMap<String, Short> NodeType = new aMap<String, Short>();
	
	public static Supplier<aNode<Node>> newDOM_Node = () -> aDOM_NODE();

	static {
		NodeType.put("NODE",0);
		NodeType.put("ELEMENT", 1);
		NodeType.put("ATTRIBUTE", 2);
		NodeType.put("TEXT", 3);
		NodeType.put("CDATA_SECTION", 4);
		NodeType.put("ENTITY_REFERENCE", 5);
		NodeType.put("ENTITY", 6);
		NodeType.put("PROCESSING_INSTRUCTION", 7);
		NodeType.put("COMMENT", 8);
		NodeType.put("DOCUMENT", 9);
		NodeType.put("DOCUMENT_TYPE", 10);
		NodeType.put("DOCUMENT_FRAGMENT", 11);
		NodeType.put("NOTATION", 12);
	}


	protected static aNode<Node> aDOM_NODE()
	{
		aNode<Node> N = new aNode<Node>();
		N.shared.put("Name",N.label);
		
		return N;		
	}
	
	
	
	public aNode<Element> ElementNode() {
		aNode<Node> N = Dom_Imp.newDOM_Node.get();
		aNode<Element> E = new aNode<Element>();
		
		
		
		
		return E;
	}

}
