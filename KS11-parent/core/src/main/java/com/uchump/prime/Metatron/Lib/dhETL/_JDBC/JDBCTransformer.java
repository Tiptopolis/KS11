package com.uchump.prime.Metatron.Lib.dhETL._JDBC;


import com.lmax.disruptor.EventHandler;
import com.uchump.prime.Metatron.Lib.dhETL.Row;
import com.uchump.prime.Metatron.Lib.dhETL.A_I._Transformer;

public class JDBCTransformer implements _Transformer<Row> {


    @Override
    public void onEvent(Row event, long sequence, boolean endOfBatch) throws Exception {

        System.out.print(sequence+"\t"+endOfBatch+"\t"+event);
    }
}