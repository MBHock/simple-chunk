package de.hock.batch.processing;

import javax.batch.api.listener.JobListener;
import javax.inject.Inject;
import javax.inject.Named;
import java.time.Duration;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
public class JobListenerImpl implements JobListener {

    @Inject
    private Logger logger;
    private Instant jobStart;


    @Override
    public void beforeJob() throws Exception {
        jobStart = Instant.now();
        logger.log(Level.INFO, "The job is starting at {0}", jobStart);
    }

    @Override
    public void afterJob() throws Exception {
        Instant jobEnd = Instant.now();
        logger.log(Level.INFO, "The job is ended at {0}", jobEnd);
        Duration duration = Duration.between(jobStart, jobEnd);
        logger.log(Level.INFO, "Total execution time={0} ", duration);
    }

}