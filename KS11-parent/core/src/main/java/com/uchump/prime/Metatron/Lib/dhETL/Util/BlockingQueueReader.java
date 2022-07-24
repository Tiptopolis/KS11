package com.uchump.prime.Metatron.Lib.dhETL.Util;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import com.lmax.disruptor.RingBuffer;
import com.uchump.prime.Metatron.Lib.dhETL.Field;
import com.uchump.prime.Metatron.Lib.dhETL.Row;
import com.uchump.prime.Metatron.Lib.dhETL.A_I._Reader;


public class BlockingQueueReader implements _Reader<Row> {

    private BlockingQueue<Row> queue;
    private boolean hasRemaining = true;
    private RingBuffer<Row> ringBuffer ;
    private Field field ;


    @Override
    public void read() throws Exception {

        long sequence = ringBuffer.next();  // Grab the next sequence
        try {
            Row row = ringBuffer.get(sequence); // Get the entry in the Disruptor
            if(!row.isCanWrite()) row.setCanWrite(true);
            if(row.getField()==null&&this.field!=null) {
                row.setField((Field)field.cpy());
            }
            // for the sequence
            if(!setRow(row)){
                hasRemaining=false;
                System.out.println("hasRemaining:"+hasRemaining);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            if(hasRemaining) ringBuffer.publish(sequence);
        }
    }

    private boolean setRow(Row row) throws InterruptedException {
        if(this.queue==null) throw new RuntimeException("ERR:queue can not be null!");
        Row out = this.queue.take();
        if(out.isBlank()) return  false;

        row.setColumn(out);
        out = null;
        return true;
    }

    @Override
    public void setRingBuffer(RingBuffer ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    @Override
    public boolean hasRemaining() {
        return hasRemaining;
    }

    @Override
    public _Reader<Row> setField(Field field) {
        this.field = field;
        return this;
    }

    @Override
    public void before() throws Exception {

    }

    @Override
    public void after() throws Exception {

    }

    public Queue<Row> getQueue() {
        return queue;
    }

    public BlockingQueueReader setQueue(BlockingQueue<Row> queue) {
        this.queue = queue;
        return this;
    }
}