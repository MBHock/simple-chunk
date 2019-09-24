package de.hock.batch.processing;

import javax.batch.api.chunk.ItemWriter;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:Mojammal.Hock@gmail.com">Mojammal Hock</a>
 */
@Named("SimpleFileWriter")
public class SimpleFileWriter implements ItemWriter {

    @Inject
    private JobProperties jobProperties;

    @Inject
    private Logger logger;

    @Inject
    private AsychronousWriter asychronousWriter;

    /* (non-Javadoc)
     * @see javax.batch.api.chunk.ItemWriter#open(java.io.Serializable)
     */
    @Override
    public void open(Serializable checkpoint) throws Exception {
        Path path = Paths.get(jobProperties.getOutputDirectory(), jobProperties.getOutputFilename());
        asychronousWriter.open(path);
        logger.log(Level.INFO, "Opening the file {0} to write output.", path);
    }

    /* (non-Javadoc)
     * @see javax.batch.api.chunk.ItemWriter#close()
     */
    @Override
    public void close() throws Exception {
//        do nothing
//        Path path = Paths.get(jobProperties.getOutputDirectory(), jobProperties.getOutputFilename());
//        logger.log(Level.INFO, "Closing the file writer to file {0}", path);
//
//        if(bufferedWriter != null) {
//            bufferedWriter.close();
//        }
    }

    /* (non-Javadoc)
     * @see javax.batch.api.chunk.ItemWriter#writeItems(java.util.List)
     */
    @Override
    public void writeItems(List<Object> items) throws Exception {
        logger.log(Level.FINE, "{0} number of items will be written", items.size());
        String lines = items.stream().map(String::valueOf).collect(Collectors.joining(System.lineSeparator()));
        asychronousWriter.write(lines.concat(System.lineSeparator()));
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
