package com.uchump.prime.Metatron;

import static com.uchump.prime.Core.uAppUtils.*;
import static com.uchump.prime.Core.uSketcher.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.uchump.prime.uChumpEngine;
import com.uchump.prime.Core.GdxFileUtils;
import com.uchump.prime.Core.Primitive.Struct.aList;
import com.uchump.prime.Core.Primitive.Struct.aMultiMap;
import com.uchump.prime.Core.Primitive.Struct.aSet;
import com.uchump.prime.Core.Primitive.Struct.aMap;
import com.uchump.prime.Core.Utils.StringUtils;
import com.uchump.prime.Metatron.Lib.exp4M.tokenizer.Tokens;

public class _GenerateRaw {

	public static aSet<String> WORDS = new aSet<String>();
	public static aMultiMap<Character,String> WORDS_ALPHABETIC = new aMultiMap<Character,String>();
	public static aMultiMap<String,String> WORD = new aMultiMap<String,String>();
	

	public static void _WORDS() {
		InputStream input = uChumpEngine.class.getResourceAsStream("/data/WORDS.txt");
		InputStreamReader inputReader = new InputStreamReader(input);
		aSet<String> result;
		BufferedReader br = new BufferedReader(inputReader);
		String line = null;
		int c = 0;
		try {
			while ((line = br.readLine()) != null) {
				// System.out.println(line);
				WORDS.append(line);
				WORDS_ALPHABETIC.put(line.charAt(0),line);
				c++;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		//process_WORDS();
	}

	private static void process_WORDS() {
		for (int o = 0; o < WORDS.size(); o++) {
			String O = WORDS.get(o);
			for (String I : WORDS) 
				for(int i =0; i < I.length(); i++)
				{
					String w = I.substring(0, i);
					if(StringUtils.startsWith(O, w));
					WORD.put(w,O);
				}
			
		}

	}
}
