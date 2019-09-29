package de.hock.batch.processing;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.ItemReader;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedReader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import static de.hock.batch.processing.JobProperties.PARTITION_FILENAME;

/**
 * @author <a href="mailto:Mojammal.Hock@gmail.com">Mojammal Hock</a>
 */
@Tracing
@Named("SimpleFileReader")
public class SimpleFileReader implements ItemReader {

    @Inject
    @BatchProperty(name = PARTITION_FILENAME)
    private String fileName;

    @Inject
    private Logger logger;

    private BufferedReader bufferedReader;

    /* (non-Javadoc)
     * @see javax.batch.api.chunk.ItemReader#open(java.io.Serializable)
     */
    @Override
    public void open(Serializable checkpoint) throws Exception {
//        reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileNmae))));
        bufferedReader = Files.newBufferedReader(Paths.get(fileName));
    }

    /* (non-Javadoc)
     * @see javax.batch.api.chunk.ItemReader#close()
     */
    @Override
    public void close() throws Exception {
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
