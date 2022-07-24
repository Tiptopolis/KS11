package com.uchump.prime.Metatron.Lib.dhETL.A_I;

import com.lmax.disruptor.*;
import com.uchump.prime.Metatron.Lib.dhETL.Field;

import java.sql.SQLException;

public interface _Reader<E> extends Ready_ {

	public void read() throws Exception;

	public void setRingBuffer(RingBuffer ringBuffer);

	public boolean hasRemaining();

	public _Reader<E> setField(Field field);

}