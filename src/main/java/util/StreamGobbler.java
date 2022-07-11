package util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.cert.CertificateRevokedException;
import java.util.function.Consumer;

// Thanks to https://www.baeldung.com/run-shell-command-in-java for this class.

public class StreamGobbler implements Runnable {
    private InputStream inputStream;
    private InputStream errorStream;
    private Consumer<String> consumer;

    public StreamGobbler(InputStream inputStream, InputStream errorStream, Consumer<String> consumer) {
        this.inputStream = inputStream;
        this.errorStream = errorStream;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        new BufferedReader(new InputStreamReader(inputStream)).lines()
                .forEach(consumer);
        new BufferedReader(new InputStreamReader(errorStream)).lines()
                .forEach(consumer);
    }
}
