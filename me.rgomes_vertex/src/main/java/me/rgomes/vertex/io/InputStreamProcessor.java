package me.rgomes.vertex.io;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.concurrent.Callable;

public abstract class InputStreamProcessor<R extends Reader, W extends Writer>
        implements Callable<InputStream> {

    protected final InputStream is;

    //
    // protected constructors
    //

    protected InputStreamProcessor(final InputStream is) {
        this.is = is;
    }


    //
    // protected abstract methods
    //

    protected abstract R buildReader(final InputStream inputStream);
    protected abstract W buildWriter(final OutputStream outputStream);
    protected abstract void process(final R reader, final W writer);



    //
    // implements Callable
    //

    @Override
    public InputStream call() throws Exception {
        // obtain data we are interested via Reader r
        final R r = buildReader(is);
        // write transformed data to Writer w
        final PipedOutputStream pos = new PipedOutputStream();
        final W w = buildWriter(pos);
        // create a PipepInputStream and connect to a PipedOutputStream
        final PipedInputStream pis =  new PipedInputStream(pos);
        // transform data
        process(r, w);
        // return a InputStream to caller via PipedInputStream pis
        return pis;
    }

}
