package de.hock.batch.processing;

import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:Mojammal.Hock@gmail.com">Mojammal Hock</a>
 */
@Named("SimpleFileProcessor")
public class SimpleFileProcessor implements ItemProcessor {

    @Inject
    private Logger logger;

    /* (non-Javadoc)
     * @see javax.batch.api.chunk.ItemProcessor#processItem(java.lang.Object)
     */
    @Override
    public Object processItem(Object item) throws Exception {
        String response = null;

        String itemToBeProcesssed = (String) item;
        if(!"".equals(itemToBeProcesssed)) {
            response = "We prcssed the object [" + item + "]";
            logger.log(Level.FINE, "Process: " + response);
        }

        return response;
    }

}
