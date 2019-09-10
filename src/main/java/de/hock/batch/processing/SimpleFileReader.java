package de.hock.batch.processing;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.ItemReader;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedReader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:Mojammal.Hock@gmail.com">Mojammal Hock</a>
 */
@Named("SimpleFileReader")
public class SimpleFileReader implements ItemReader {

    @Inject
    @BatchProperty(name = "fileName")
    private String fileNmae;

    @Inject
    private Logger logger;

    private BufferedReader bufferedReader;

    /* (non-Javadoc)
     * @see javax.batch.api.chunk.ItemReader#open(java.io.Serializable)
     */
    @Override
    public void open(Serializable checkpoint) throws Exception {
        logger.log(Level.INFO, "Opening the {0} to read ", fileNmae);
//        reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileNmae))));
        bufferedReader = Files.newBufferedReader(Paths.get(fileNmae));
    }

    /* (non-Javadoc)
     * @see javax.batch.api.chunk.ItemReader#close()
     */
    @Override
    public void close() throws Exception {
        logger.log(Level.INFO, "Closing the file {0}", fileNmae);
        if(bufferedReader != null) {
            bufferedReader.close();
        }
    }

    /* (non-Javadoc)
     * @see javax.batch.api.chunk.ItemReader#readItem()
     */
    @Override
    public Object readItem() throws Exception {
        String s = bufferedReader.readLine();
        return s;
    }

    /* (non-Javadoc)
     * @see javax.batch.api.chunk.ItemReader#checkpointInfo()
     */
    @Override
    public Serializable checkpointInfo() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}
