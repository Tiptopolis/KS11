package com.uchump.prime.Metatron.Lib.dhETL.A_I;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;

public interface _Writer<E> extends Ready_,WorkHandler<E> {
    public void setDisruptor(Disruptor<E> disruptor);
}