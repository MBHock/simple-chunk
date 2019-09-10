package de.hock.batch.processing;

import javax.batch.api.chunk.ItemWriter;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:Mojammal.Hock@gmail.com">Mojammal Hock</a>
 */
@Named("SimpleFileWriter")
public class SimpleFileWriter implements ItemWriter {

    private static final String filename = "/Users/mojammalhock/tmp/test-chunk-sampledata.txt";
    private BufferedWriter bufferedWriter;

    @Inject
    private JobAndStepPropertyReader jobAndStepPropertyReader;

    @Inject
    private Logger logger;

    /* (non-Javadoc)
     * @see javax.batch.api.chunk.ItemWriter#open(java.io.Serializable)
     */
    @Override
    public void open(Serializable checkpoint) throws Exception {
        String outputDirectory = jobAndStepPropertyReader.getProperty("outputDirectory");
        Path path = Paths.get(outputDirectory, "chunk_write_result.txt");
        logger.log(Level.INFO, "Opening file {0} to write", path);

        bufferedWriter = Files.newBufferedWriter(path);
    }

    /* (non-Javadoc)
     * @see javax.batch.api.chunk.ItemWriter#close()
     */
    @Override
    public void close() throws Exception {
        logger.log(Level.INFO, "Closing the bufferedwriter");

        if(bufferedWriter != null) {
            bufferedWriter.close();
        }
    }

    /* (non-Javadoc)
     * @see javax.batch.api.chunk.ItemWriter#writeItems(java.util.List)
     */
    @Override
    public void writeItems(List<Object> items) throws Exception {
        logger.log(Level.INFO, "{0} number of items will be written", items.size());

        items.forEach(line -> {
            try {
                bufferedWriter.write(String.valueOf(line));
                bufferedWriter.newLine();
            }
            catch(IOException e) {
                System.out.println(e.getCause());
            }
        });
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
