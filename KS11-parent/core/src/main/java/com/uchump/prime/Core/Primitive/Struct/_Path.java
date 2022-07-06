package com.uchump.prime.Core.Primitive.Struct;

import java.lang.reflect.Method;

import com.uchump.prime.Core.Utils.StringUtils;

public class _Path<T> extends aLinkedList<T> {

	// first char dominant
	public static final String Space = " ";
	public static final String _NEXT = ".";
	public static final String NEXT_ = ",";
	public static final String LOOKUP_ = "_";
	public static final String LOOK_UP = "/";
	public static final String _IS_TO_ = "|";

	public static final String stdDelimiters = " .,/";

	private static enum TYPE {
		SPACE(Space), INNER(_NEXT), OUTTER(NEXT_), _LOOKUP(LOOKUP_), LOOKUP(LOOK_UP), IS_TO(_IS_TO_);

		String Type = Space;

		TYPE(String s) {
			this.Type = s;
		}
	};

	private String value = "";

	private aMap<String, Integer> ent;

	public _Path(String path) {
		// get min of first indices of each delimiter
		aMap<String, Integer> R = new aMap<String, Integer>();
		// R.put(Space,StringUtils.split(path, Space));
		R.put(Space, path.indexOf(Space));
		R.put(_NEXT, path.indexOf(_NEXT));
		R.put(NEXT_, path.indexOf(NEXT_));
		R.put(LOOK_UP, path.indexOf(LOOK_UP));

	}

	private aLinkedList<String> parse(String s) {
		return null;

	}
}
