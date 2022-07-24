package com.uchump.prime.Core.Primitive.Struct.Graph;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import com.uchump.prime.Core.Primitive.Struct._Map;
import com.uchump.prime.Metatron.Lib.exp4M.tokenizer.Tokens;
import com.uchump.prime.Metatron.Lib.exp4M.tokenizer._Token;

public class aName extends Tokens._Word {

	private static final String NAME_START_CHAR = "_A-Za-z\u00C0-\u00D6\u00D8-\u00F6\u00F8-\u02FF\u0370-\u037D\u037F-\u1FFF\u200C-\u200D\u2070-\u218F\u2C00-\u2FEF\u3001-\uD7FF\uF900-\uFDCF\uFDF0-\uFFFD";
	private static final String NAME_CHAR = NAME_START_CHAR + "-.0-9\u00B7\u0300-\u036F\u203F-\u2040";
	private static final String NCNAME = "[" + NAME_START_CHAR + "][" + NAME_CHAR + "]*";
	private static final Pattern RE_NAME = Pattern.compile("[:" + NAME_START_CHAR + "][:" + NAME_CHAR + "]*");
	private static final Pattern RE_NCNAME = Pattern.compile(NCNAME);
	private static final Pattern RE_QNAME = Pattern.compile("(?:" + NCNAME + ":)?" + NCNAME);

	private String name;
	private String context; // namespace
	private int hash = -1;
	public Supplier<String> getName = () -> {
		if (this.name != null)
			return this.name;
		else
			return this.get();
	};
	public Supplier<Integer> getHashCode = () -> {
		if (this.hash != -1)
			return this.hash;
		else
			return this.hashCode();
	};

	public _Map.Entry<String, Object> Namespace;

	public aName(String name) {
		super(name);
	}

	public aName(String name, String context) {
		super(name);
	}
}
