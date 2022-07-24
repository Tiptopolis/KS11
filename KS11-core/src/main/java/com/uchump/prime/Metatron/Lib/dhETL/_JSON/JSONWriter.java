package com.uchump.prime.Metatron.Lib.dhETL._JSON;

import com.lmax.disruptor.dsl.Disruptor;
import com.uchump.prime.Metatron.Lib.dhETL.Row;
import com.uchump.prime.Metatron.Lib.dhETL.A_I.Each_;
import com.uchump.prime.Metatron.Lib.dhETL.A_I._Writer;


public class JSONWriter implements _Writer<Row>,Each_<Row> {
    @Override
    public void dealEach(Row row) throws Exception {

    }

    @Override
    public void setDisruptor(Disruptor<Row> disruptor) {

    }

    @Override
    public void before() throws Exception {

    }

    @Override
    public void after() throws Exception {

    }

    @Override
    public void onEvent(Row row) throws Exception {

    }
}