package com.uchump.prime.Metatron.Lib.dhETL._CSV;

import java.io.BufferedWriter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.lmax.disruptor.dsl.Disruptor;
import com.uchump.prime.Metatron.Lib.dhETL.Field;
import com.uchump.prime.Metatron.Lib.dhETL.Row;
import com.uchump.prime.Metatron.Lib.dhETL.A_I.Each_;
import com.uchump.prime.Metatron.Lib.dhETL.A_I._Writer;


public class CSVWriter implements _Writer<Row>,Each_<Row>{

    private BufferedWriter ioWriter ;
    private Field field ;
    private boolean hasWriteHeader = false;
    private CSVPrinter csvPrinter;
    private CSVFormat csvFormat = CSVFormat.MYSQL;
    @Override
    public void setDisruptor(Disruptor<Row> disruptor) {

    }

    @Override
    public void before() throws Exception {
        csvPrinter = new CSVPrinter(ioWriter, csvFormat);
    }

    @Override
    public void after() throws Exception {
        ioWriter.close();
    }

    @Override
    public void onEvent(Row row) throws Exception {

        try {
            if(!row.isCanWrite()) return;
            dealEach(row);
            if(this.field==null) this.field = row.getField();

            int size= field.size();
            StringBuffer buffer = new StringBuffer();
            if(!hasWriteHeader) {
                //write header
                String ks [] =field.toStringArray();

                csvPrinter.printRecord(ks);
                hasWriteHeader=true;
            }

            csvPrinter.printRecord(row.toStringArray());

        }catch (Exception e){
            e.printStackTrace();
            csvPrinter.close();
            System.exit(-1);//emergency exit
//            disruptor.shutdown(3, TimeUnit.SECONDS);

            throw new Exception(e);
        }finally {
            if(!row.isCanRead()) row.setCanRead(true);
        }
    }

    public java.io.Writer getIoWriter() {
        return ioWriter;
    }

    public CSVWriter setIoWriter(java.io.Writer ioWriter) {
        this.ioWriter = new BufferedWriter(ioWriter);
        return this;
    }

    @Override
    public void dealEach(Row row) throws Exception {

    }


    public CSVFormat getCsvFormat() {
        return csvFormat;
    }

    public CSVWriter setCsvFormat(CSVFormat csvFormat) {
        this.csvFormat = csvFormat;
        return this;
    }
}