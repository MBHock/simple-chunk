package de.hock.batch.processing;

import javax.enterprise.context.ApplicationScoped;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@ApplicationScoped
public class AsychronousWriter {

    private BufferedWriter bufferedWriter;

    public void open(Path path) throws IOException {

        if(bufferedWriter == null) {
            bufferedWriter = Files.newBufferedWriter(path);
        }
    }

    public void write(String lines) throws IOException {
        Objects.requireNonNull(bufferedWriter, () -> "The caller must call AsychronousWriter#open(path) before writing.");
        bufferedWriter.write(lines);
        bufferedWriter.flush();
    }

    public void close() throws IOException {
        if(bufferedWriter != null) {
            bufferedWriter.close();
        }
    }

}
