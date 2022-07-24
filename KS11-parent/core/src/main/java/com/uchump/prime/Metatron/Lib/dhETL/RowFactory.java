package com.uchump.prime.Metatron.Lib.dhETL;

import com.uchump.prime.Metatron.Lib.dhETL.A_I._Factory;

public class RowFactory implements _Factory<Row> {
    @Override
    public Row newInstance() {
        return new Row();
    }
}