package com.uchump.prime.Core.Data;


import com.uchump.prime.Core.Data.Primitive.aCache;
import com.uchump.prime.Metatron.Lib.dhETL.Model;
import com.uchump.prime.Metatron.Lib.dhETL._JDBC.JDBCReader;
import com.uchump.prime.Metatron.Lib.dhETL._JDBC.JDBCWriter;

public class TableManager<T> extends aCache<T>/*implements iCRUD<I,T>*/{


	//column signiture
	//aSet<aSet> members;
	//_Table cache;
	Model table;
	JDBCReader reader;
	JDBCWriter writer;
	//SQL->Model->Entity



}
