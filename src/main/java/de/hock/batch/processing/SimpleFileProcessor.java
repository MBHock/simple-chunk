package de.hock.batch.processing;

import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:Mojammal.Hock@gmail.com">Mojammal Hock</a>
 */

@Named("SimpleFileProcessor")
public class SimpleFileProcessor implements ItemProcessor {

    public static final String EMPTY_STRING = "";

    @Inject
    private Logger logger;

    /* (non-Javadoc)
     * @see javax.batch.api.chunk.ItemProcessor#processItem(java.lang.Object)
     */
    @Tracing(LogLevel.FINEST)
    @Override
    public Object processItem(Object item) throws Exception {
        String response = null;

        String itemToBeProcesssed = (String) item;

        if(!Objects.equals(EMPTY_STRING, itemToBeProcesssed)) {
            response = itemToBeProcesssed;
        }

        return response;
    }

}
