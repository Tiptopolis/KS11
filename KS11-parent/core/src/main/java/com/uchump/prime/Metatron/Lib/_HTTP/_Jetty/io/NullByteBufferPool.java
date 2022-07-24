package com.uchump.prime.Metatron.Lib._HTTP._Jetty.io;

import java.nio.ByteBuffer;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.BufferUtil;

public class NullByteBufferPool implements ByteBufferPool {
	private final RetainableByteBufferPool _retainableByteBufferPool = RetainableByteBufferPool.from(this);

	@Override
	public ByteBuffer acquire(int size, boolean direct) {
		if (direct)
			return BufferUtil.allocateDirect(size);
		else
			return BufferUtil.allocate(size);
	}

	@Override
	public void release(ByteBuffer buffer) {
		BufferUtil.clear(buffer);
	}

	@Override
	public RetainableByteBufferPool asRetainableByteBufferPool() {
		return _retainableByteBufferPool;
	}
}