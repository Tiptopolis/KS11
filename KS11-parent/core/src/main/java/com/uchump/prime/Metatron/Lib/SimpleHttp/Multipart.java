package com.uchump.prime.Metatron.Lib.SimpleHttp;
import java.io.File;

import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.Headers;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.HttpPost;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.HttpRequestVisitor;

import static java.lang.String.format;

public class Multipart implements HttpPost {
	
	private final String name;
	private final File file;

	public Multipart(String name, File file) {
		this.name = name;
		this.file = file;
	}

	public File getFile() {
		return file;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public void accept(HttpRequestVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public MultipartContent getContent() {
		return new MultipartContent(name, file);
	}

	@Override
	public Headers getHeaders() {
//		return headers(header("Content-Type", "multipart/form-data;boundary=\"boundary\""));
		return EmptyHeaders.emptyHeaders();
	}

	@Override
	public String toString() {
		return format("Multipart{content='name=%s, filename=%s', headers='%s'}", name, file, getHeaders());
	}
}