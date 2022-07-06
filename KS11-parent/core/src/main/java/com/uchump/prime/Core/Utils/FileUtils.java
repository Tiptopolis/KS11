package com.uchump.prime.Core.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import com.uchump.prime.Core.Primitive.aValue;
import com.uchump.prime.Core.Primitive.A_I.iCollection;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Primitive.Struct.aMap;

public class FileUtils {


	public static aMap<String, aList<String>> mapFolder(File folder) {
		aMap<String, aList<String>> dirFiles = new aMap<String, aList<String>>();
		aList<aMap<String, aList<String>>> rec = new aList<aMap<String, aList<String>>>();
		if (folder.isDirectory()) {

			aList<String> fileNames = new aList<String>();
			
			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.isDirectory()) {
					rec.append(mapFolder(fileEntry));
				} else {
					String fileName = fileEntry.getName();
					fileNames.append(fileName);
				}
			}
			dirFiles.put(folder.getPath(),fileNames);

		}
		
		if(!rec.isEmpty())
			for(aMap<String, aList<String>> m : rec)
				dirFiles.put(m.getEntries());
		return dirFiles;
	}
	
	//PROPERTIES
	
	public static Properties mapProperties(aValue... values) {
		Properties res = new Properties();
		for (aValue v : values) {
			res.put(v.label(), v.get());
		}

		return res;
	}
	
	public static Properties mapProperties(Object... values) {
		Properties res = new Properties();
		for (int i = 0; i < values.length; i++) {
			res.put(i, values[i]);
		}

		return res;
	}

	public static Properties mapProperties(iCollection<aValue> values) {
		Properties res = new Properties();
		for (aValue v : values) {
			res.put(v.label(), v.get());
		}

		return res;
	}

	public static Properties listProperties(iCollection<Object> values) {
		Properties res = new Properties();
		for (int i = 0; i < values.size(); i++) {
			res.put(i, values.get(i));
		}

		return res;
	}

	public static Properties listProperties(Object... values) {
		Properties res = new Properties();
		for (int i = 0; i < values.length; i++) {
			res.put("" + i, values[i]);
		}

		return res;
	}

}
