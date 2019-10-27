package de.hock.batch.processing;

import javax.batch.api.listener.JobListener;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named("InfoJobListener")
public class InfoJobListener implements JobListener {

    @Inject
    private Logger logger;

    @Override
    public void beforeJob() throws Exception {
        String collect = System.getProperties().
                entrySet().stream().map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue())).collect(Collectors.joining(System.lineSeparator()));
        logger.log(Level.INFO, "The job is starting");
        logger.log(Level.INFO, "Properties \n {0}", collect);
    }

    @Override
    public void afterJob() throws Exception {
        logger.log(Level.INFO, "The job is finished");
    }

}
