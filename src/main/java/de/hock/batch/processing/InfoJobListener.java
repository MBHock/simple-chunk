package de.hock.batch.processing;

import javax.batch.api.listener.JobListener;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named("InfoJobListener")
public class InfoJobListener implements JobListener {

    @Inject
    private Logger logger;

    @Override
    public void beforeJob() throws Exception {
        String collect = System.getProperties().entrySet().stream().map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue())).collect(Collectors.joining(System.lineSeparator()));

        //                forEach(entry -> logger.log(Level.INFO, "Key={0}, value={1}", new Object[]{entry.getKey(), entry.getValue()}) );
        logger.log(Level.INFO, "The job is starting");
        logger.log(Level.INFO, "Properties \n {0}", collect);
        new InputStreamReader();

    }

    @Override
    public void afterJob() throws Exception {
        logger.log(Level.INFO, "The job is finished");
    }

}
