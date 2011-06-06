package me.rgomes.vertex.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


public abstract class BufferedInputStreamProcessor
        extends InputStreamProcessor<BufferedReader, BufferedWriter> {

    //
    // protected constructors
    //

    protected BufferedInputStreamProcessor(final InputStream is) {
        super(is);
    }

    //
    // protected abstract methods
    //

    protected abstract String transform(final String text);


    //
    // overrides AbstractInputStreamProcessor
    //

    @Override
    protected BufferedReader buildReader(final InputStream inputStream) {
        // obtain data we are interested via BufferedReader
        return new BufferedReader(new InputStreamReader(is));
    }

    @Override
    protected BufferedWriter buildWriter(final OutputStream outputStream) {
        // write expanded properties to BufferedWriter
        return new BufferedWriter(new OutputStreamWriter(outputStream));
    }

    @Override
    protected void process(final BufferedReader br, final BufferedWriter bw) {
        // process data in a separate Thread
        new Thread(new Runnable() {
            public void run() {
                try {
                    String text;
                    while ((text = br.readLine()) != null) {
                        final String result = transform(text);
                        bw.append(result).append("\n");
                    }
                } catch (final IOException ex1) {
                    throw new RuntimeException("Error occured while applying text transformation", ex1);
                } finally {
                    try {
                        br.close();
                        bw.close();
                    } catch (final IOException ex2) {
                        // swallow this exception but not without complaining
                        ex2.printStackTrace(System.err);
                    }
                }
            }
        }).start();
    }

}
