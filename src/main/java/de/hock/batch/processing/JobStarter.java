package de.hock.batch.processing;

import org.jberet.runtime.JobExecutionImpl;
import org.jberet.se._private.SEBatchLogger;
import org.jberet.se._private.SEBatchMessages;

import javax.batch.operations.BatchRuntimeException;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:Mojammal.Hock@gmail.com">Mojammal Hock</a>
 */
public class JobStarter {

    /**
     * @param args
     * @throws InterruptedException
     */

    public static void main(final String[] args) throws BatchRuntimeException {
        Instant jobStart = Instant.now();

        final java.util.Properties jobParameters = new java.util.Properties();
        for(int i = 1; i < args.length; i++) {
            final int equalSignPos = args[i].indexOf('=');
            if(equalSignPos <= 0) {
                usage(args);
                return;
            }
            final String key = args[i].substring(0, equalSignPos).trim();
            final String val = args[i].substring(equalSignPos + 1).trim();
            System.out.println(String.format("prop: '%s' -> '%s'", key, val));
            jobParameters.setProperty(key, val);
        }

        final JobOperator jobOperator = BatchRuntime.getJobOperator();
        final long jobExecutionId;
        try {
            jobExecutionId = jobOperator.start("chunk-example", jobParameters);
            final JobExecutionImpl jobExecution = (JobExecutionImpl) jobOperator.getJobExecution(jobExecutionId);
            jobExecution.awaitTermination(0, TimeUnit.SECONDS);  //no timeout

            if(!jobExecution.getBatchStatus().equals(BatchStatus.COMPLETED)) {
                throw SEBatchMessages.MESSAGES.jobDidNotComplete("chunk-example",
                        jobExecution.getBatchStatus(), jobExecution.getExitStatus());
            }
        }
        catch(InterruptedException e) {
            throw new BatchRuntimeException(e);
        }
        Instant jobEnd = Instant.now();

        Duration between = Duration.between(jobStart, jobEnd);
       // System.out.println(String.format("Total job execution time: %d:%d:%d.%d", between.get(ChronoUnit.HOURS), between.get(ChronoUnit.MINUTES), between.getSeconds(), between.get(ChronoUnit.MILLIS)));
        System.out.println(String.format("Total job execution time: %s", between));
    }

    private static void usage(final String[] args) {
        SEBatchLogger.LOGGER.usage(args);
    }

}
