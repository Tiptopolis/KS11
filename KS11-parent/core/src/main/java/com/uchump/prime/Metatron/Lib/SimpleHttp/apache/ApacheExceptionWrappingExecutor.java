package com.uchump.prime.Metatron.Lib.SimpleHttp.apache;


import com.uchump.prime.Metatron.Lib.SimpleHttp.*;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.*;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

public class ApacheExceptionWrappingExecutor implements Executor<HttpException> {

    @Override
    public <V> V submit(Callable<V> callable) throws HttpException {
        try {
            return callable.call();
        } catch (ConnectTimeoutException e) {
            throw new HttpConnectionTimeoutException(e);
        } catch (SocketTimeoutException e) {
            throw new HttpSocketTimeoutException(e);
        } catch (HttpHostConnectException e) {
            throw new HttpConnectionRefusedException(e);
        } catch (UnknownHostException e) {
            throw new HttpUnknownHostException(e.getMessage(), e);
        } catch (Exception e) {
            throw new HttpException(e);
        }
    }
}