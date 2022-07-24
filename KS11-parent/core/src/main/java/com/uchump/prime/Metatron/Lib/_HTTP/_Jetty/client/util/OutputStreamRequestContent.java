package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.util;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.FutureCallback;

/**
 * <p>A {@link Request.Content} that provides content asynchronously through an {@link OutputStream}
 * similar to {@link AsyncRequestContent}.</p>
 * <p>{@link OutputStreamRequestContent} can only be used in conjunction with
 * {@link Request#send(Response.CompleteListener)} (and not with its blocking counterpart
 * {@link Request#send()}) because it provides content asynchronously.</p>
 * <p>Content must be provided by writing to the {@link #getOutputStream() output stream}
 * that must be {@link OutputStream#close() closed} when all content has been provided.</p>
 * <p>Example usage:</p>
 * <pre>
 * HttpClient httpClient = ...;
 *
 * // Use try-with-resources to autoclose the output stream.
 * OutputStreamRequestContent content = new OutputStreamRequestContent();
 * try (OutputStream output = content.getOutputStream())
 * {
 *     httpClient.newRequest("localhost", 8080)
 *             .content(content)
 *             .send(new Response.CompleteListener()
 *             {
 *                 &#64;Override
 *                 public void onComplete(Result result)
 *                 {
 *                     // Your logic here
 *                 }
 *             });
 *
 *     // At a later time...
 *     output.write("some content".getBytes());
 *
 *     // Even later...
 *     output.write("more content".getBytes());
 * } // Implicit call to output.close().
 * </pre>
 */
public class OutputStreamRequestContent extends AsyncRequestContent
{
    private final AsyncOutputStream output;

    public OutputStreamRequestContent()
    {
        this("application/octet-stream");
    }

    public OutputStreamRequestContent(String contentType)
    {
        super(contentType);
        this.output = new AsyncOutputStream();
    }

    public OutputStream getOutputStream()
    {
        return output;
    }

    private class AsyncOutputStream extends OutputStream
    {
        @Override
        public void write(int b) throws IOException
        {
            write(new byte[]{(byte)b}, 0, 1);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException
        {
            try
            {
                FutureCallback callback = new FutureCallback();
                offer(ByteBuffer.wrap(b, off, len), callback);
                callback.get();
            }
            catch (InterruptedException x)
            {
                throw new InterruptedIOException();
            }
            catch (ExecutionException x)
            {
                throw new IOException(x.getCause());
            }
        }

        @Override
        public void flush() throws IOException
        {
            OutputStreamRequestContent.this.flush();
        }

        @Override
        public void close()
        {
            OutputStreamRequestContent.this.close();
        }
    }
}