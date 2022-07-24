package com.uchump.prime.Metatron.Lib._HTTP._Jetty.io;

import java.nio.ByteBuffer;

/**
 * <p>A {@link RetainableByteBuffer} pool.</p>
 * <p>Acquired buffers <b>must</b> be released by calling {@link RetainableByteBuffer#release()} otherwise the memory they hold will
 * be leaked.</p>
 */
public interface RetainableByteBufferPool
{
    /**
     * Acquires a memory buffer from the pool.
     * @param size The size of the buffer. The returned buffer will have at least this capacity.
     * @param direct true if a direct memory buffer is needed, false otherwise.
     * @return a memory buffer with position and size set to 0.
     */
    RetainableByteBuffer acquire(int size, boolean direct);

    void clear();

    static RetainableByteBufferPool from(ByteBufferPool byteBufferPool)
    {
        return new NotRetainedByteBufferPool(byteBufferPool);
    }

    class NotRetainedByteBufferPool implements RetainableByteBufferPool
    {
        private final ByteBufferPool _byteBufferPool;

        public NotRetainedByteBufferPool(ByteBufferPool byteBufferPool)
        {
            _byteBufferPool = byteBufferPool;
        }

        @Override
        public RetainableByteBuffer acquire(int size, boolean direct)
        {
            ByteBuffer byteBuffer = _byteBufferPool.acquire(size, direct);
            RetainableByteBuffer retainableByteBuffer = new RetainableByteBuffer(byteBuffer, this::release);
            retainableByteBuffer.acquire();
            return retainableByteBuffer;
        }

        private void release(RetainableByteBuffer retainedBuffer)
        {
            _byteBufferPool.release(retainedBuffer.getBuffer());
        }

        @Override
        public void clear()
        {
        }

        @Override
        public String toString()
        {
            return String.format("NonRetainableByteBufferPool@%x{%s}", hashCode(), _byteBufferPool.toString());
        }
    }
}