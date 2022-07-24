package com.uchump.prime.Metatron;

import static com.uchump.prime.Core.uAppUtils.*;
import static com.uchump.prime.Core.uSketcher.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Properties;

import com.uchump.prime.Core.Data.DataDirector;
import com.uchump.prime.Core.Data.Util._SQL;
import com.uchump.prime.Core.Primitive.aNode;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Primitive.Struct.aMultiMap;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class MetatronData {

	

	public aMultiMap<String, DataDirector> Atlas;

	public static String localSystemPath = "C:/Users/Public/Data"; // Path, delimiter, caseMode
	public static String localInternalPath = "src/main/resources";
	public static String localDataPath = "src/main/resources";

	public static _SQL SQL;
	//http://localhost:8080/

	public MetatronData() {
		SQL = _SQL.init();
		this.Atlas = new aMultiMap<String, DataDirector>();
		// _GenerateRaw._WORDS();
		
	}
	
	
	

	private static void _FILES_() {

	}

	public static aList<String> resourceToString(String path) {

		aList<String> out = new aList<String>();
		InputStream input = Metatron.class.getResourceAsStream(path);
		InputStreamReader inputReader = new InputStreamReader(input);

		BufferedReader br = new BufferedReader(inputReader);
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				out.append(br.readLine());
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return out;
	}

	private static void _JAVA_() {

	}

	// Class->aValue
	public aNode<Class> classNode(Class c) {
		aNode<Class> C = new aNode<Class>(c);
		Method[] methods = c.getDeclaredMethods();
		for (Method m : methods) {
			C.data.put(c.getSimpleName() + "." + m.getName(), m);
		}

		return C;
	}
}
