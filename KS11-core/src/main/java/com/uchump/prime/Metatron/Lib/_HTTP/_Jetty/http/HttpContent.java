package com.uchump.prime.Metatron.Lib._HTTP._Jetty.http;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.MimeTypes.Type;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.resource.Resource;

/**
 * HttpContent interface.
 * <p>
 * This information represents all the information about a static resource that
 * is needed to evaluate conditional headers and to serve the content if need
 * be. It can be implemented either transiently (values and fields generated on
 * demand) or persistently (values and fields pre-generated in anticipation of
 * reuse in from a cache).
 * </p>
 */
public interface HttpContent {
	HttpField getContentType();

	String getContentTypeValue();

	String getCharacterEncoding();

	Type getMimeType();

	HttpField getContentEncoding();

	String getContentEncodingValue();

	HttpField getContentLength();

	long getContentLengthValue();

	HttpField getLastModified();

	String getLastModifiedValue();

	HttpField getETag();

	String getETagValue();

	ByteBuffer getIndirectBuffer();

	ByteBuffer getDirectBuffer();

	Resource getResource();

	InputStream getInputStream() throws IOException;

	ReadableByteChannel getReadableByteChannel() throws IOException;

	void release();

	Map<CompressedContentFormat, ? extends HttpContent> getPrecompressedContents();

	public interface ContentFactory {
		/**
		 * @param path      The path within the context to the resource
		 * @param maxBuffer The maximum buffer to allocated for this request. For cached
		 *                  content, a larger buffer may have previously been allocated
		 *                  and returned by the {@link HttpContent#getDirectBuffer()} or
		 *                  {@link HttpContent#getIndirectBuffer()} calls.
		 * @return A {@link HttpContent}
		 * @throws IOException if unable to get content
		 */
		HttpContent getContent(String path, int maxBuffer) throws IOException;
	}
}