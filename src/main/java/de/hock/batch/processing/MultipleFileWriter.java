package de.hock.batch.processing;

import javax.batch.api.chunk.ItemWriter;
import javax.batch.operations.BatchRuntimeException;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:Mojammal.Hock@gmail.com">Mojammal Hock</a>
 */
@Named("MultipleFileWriter")
public class MultipleFileWriter implements ItemWriter {

    @Inject
    private JobProperties jobProperties;

    @Inject
    private Logger logger;

    private static final AtomicInteger atomicInteger = new AtomicInteger(0);
    private BufferedWriter bufferedWriter;
    private Path path;

    /* (non-Javadoc)
     * @see javax.batch.api.chunk.ItemWriter#open(java.io.Serializable)
     */
    @Tracing(LogLevel.FINE)
    @Override
    public void open(Serializable checkpoint) throws Exception {
        if(Files.notExists(Paths.get(jobProperties.getOutputDirectory()))) {
            Files.createDirectories(Paths.get(jobProperties.getOutputDirectory()));
        }
        path = Paths.get(jobProperties.getOutputDirectory(), createFilename());
        bufferedWriter = Files.newBufferedWriter(path);
    }

    private String createFilename() {
        String outputFilename = jobProperties.getOutputFilename();
        int indexOfDot = outputFilename.lastIndexOf(".");
        String filename = outputFilename.substring(0, indexOfDot);
        String fileExtension = outputFilename.substring(indexOfDot);
        outputFilename = filename + "_" + atomicInteger.getAndIncrement() + fileExtension;
        return outputFilename;
    }

    /* (non-Javadoc)
     * @see javax.batch.api.chunk.ItemWriter#close()
     */
    @Tracing(LogLevel.FINE)
    @Override
    public void close() throws Exception {
        if(bufferedWriter != null) {
            bufferedWriter.close();
        }
    }

    /* (non-Javadoc)
     * @see javax.batch.api.chunk.ItemWriter#writeItems(java.util.List)
     */
    @Tracing(LogLevel.FINEST)
    @Override
    public void writeItems(List<Object> items) throws Exception {
        items.stream().map(String::valueOf).forEach(this::writeItem);
    }

    private void writeItem(String line) {
        try {
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        }
        catch(IOException e) {
            throw new BatchRuntimeException(e);
        }

    }

    /* (non-Javadoc)
     * @see javax.batch.api.chunk.ItemWriter#checkpointInfo()
     */
    @Override
    public Serializable checkpointInfo() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}
