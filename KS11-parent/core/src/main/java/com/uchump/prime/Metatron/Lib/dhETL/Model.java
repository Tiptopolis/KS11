package com.uchump.prime.Metatron.Lib.dhETL;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.uchump.prime.Metatron.Lib.dhETL.A_I.Ready_;
import com.uchump.prime.Metatron.Lib.dhETL.A_I._Factory;
import com.uchump.prime.Metatron.Lib.dhETL.A_I._Reader;
import com.uchump.prime.Metatron.Lib.dhETL.A_I._Transformer;
import com.uchump.prime.Metatron.Lib.dhETL.A_I._Writer;

import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by dog on 4/6/17.
 */
final public class Model<E> implements Ready_ {
    
    private _Reader<E> reader ;
    private _Writer<E> []writer;
    private _Transformer<E> transformer;
    private _Factory<E> factory;
    private int ringBufferSize=2^10;
    private Disruptor<E> disruptor ;
    private int printInterval = 1000;

    public _Factory<E> getFactory() {
        return factory;
    }

    public Model setFactory(_Factory<E> factory) {
        this.factory = factory;
        return this;
    }


    public Model setRingBufferSize(int ringBufferSize) {
        this.ringBufferSize = ringBufferSize;
        return this;
    }

    public Model setReader(_Reader<E> reader) {
        this.reader = reader;
        return this;
    }

    public Model setWriter(_Writer<E> []writer) {
        this.writer = writer;
        return this;
    }

    public Model setTransformer(_Transformer<E> transformer){
        this.transformer = transformer;
        return this;
    }

    public Disruptor<E> getDisruptor() {
        return disruptor;
    }

    public Model setDisruptor(Disruptor<E> disruptor) {
        this.disruptor = disruptor;
        return this;
    }

    public void start() throws Exception {

        try {
            before();
            long startTime = System.currentTimeMillis();
            reader.before();

            if(writer==null )writer=new _Writer[]{};
            for (_Writer w : writer) {
                w.before();
            }
            Executor executor = Executors.newCachedThreadPool();
//            disruptor = new Disruptor<E>(getFactory(),ringBufferSize,)
            disruptor = new Disruptor<E>(getFactory(),
                    ringBufferSize,
                    executor,
                    ProducerType.SINGLE,
                    new YieldingWaitStrategy());
            if(transformer==null){
                transformer = new _Transformer<E>() {
                    @Override
                    public void onEvent(E event, long sequence, boolean endOfBatch) throws Exception {

                    }
                };
            }
            disruptor.handleEventsWith(transformer);
            if(writer.length!=0) {
                disruptor.handleEventsWithWorkerPool(writer);
            }
            disruptor.start();


            for (_Writer w : writer) {
                w.setDisruptor(disruptor);
            }


            RingBuffer<E> ringBuffer = disruptor.getRingBuffer();
            reader.setRingBuffer(ringBuffer);


            long count = 0;
            for (long l = 0; reader.hasRemaining(); l++) {
                reader.read();
                if (l % printInterval == 0) {
                    System.out.println("read rows:\t" + l + "\tcost " + (System.currentTimeMillis() - startTime));
                }
                count = l;
            }
            long endTime = System.currentTimeMillis();
            System.out.println("total read rows:\t" + count + "\tcost " + (endTime - startTime));
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception(e);
        }finally {
            System.out.println("finally");
            if(disruptor!=null) disruptor.shutdown(60,TimeUnit.MINUTES);
            reader.after();
            for (_Writer w : writer) w.after();
        }


    }


    @Override
    public void before() throws Exception {
        if(this.factory==null) setFactory((_Factory<E>) new RowFactory());
    }

    @Override
    public void after() throws Exception {

    }

    public Model setPrintInterval(int printInterval) {
        this.printInterval = printInterval;
        return this;
    }
}