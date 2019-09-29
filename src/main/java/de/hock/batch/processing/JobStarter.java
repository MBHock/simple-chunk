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
import java.util.Arrays;
import java.util.Properties;
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
        System.out.println(Arrays.deepToString(args));

        Instant jobStart = Instant.now();

        final String jobXML = readJobXmlFilename(args);
        if(jobXML != null) {
            final Properties jobParameters = new Properties();

            if(addValidArgumentToProps(args, jobParameters)) {
                startJob(jobXML, BatchRuntime.getJobOperator(), jobParameters);
            }
        }

        Duration between = Duration.between(jobStart, Instant.now());
        System.out.println(String.format("Total job execution time: %s", between));
        // System.out.println(String.format("Total job execution time: %d:%d:%d.%d", between.get(ChronoUnit.HOURS), between.get(ChronoUnit.MINUTES), between.getSeconds(), between.get(ChronoUnit.MILLIS)));
    }

    private static void startJob(String jobXML, JobOperator jobOperator, Properties jobParameters) {
        try {
            final long jobExecutionId = jobOperator.start(jobXML, jobParameters);

            final JobExecutionImpl jobExecution = (JobExecutionImpl) jobOperator.getJobExecution(jobExecutionId);
            jobExecution.awaitTermination(0, TimeUnit.SECONDS);  //no timeout

            if(!jobExecution.getBatchStatus().equals(BatchStatus.COMPLETED)) {
                throw SEBatchMessages.MESSAGES.jobDidNotComplete(jobXML,
                        jobExecution.getBatchStatus(), jobExecution.getExitStatus());
            }
        }
        catch(InterruptedException e) {
            throw new BatchRuntimeException(e);
        }
    }

    private static boolean addValidArgumentToProps(String[] args, Properties jobParameters) {
        for(int i = 1; i < args.length; i++) {
            final int equalSignPos = args[i].indexOf('=');
            if(equalSignPos <= 0) {
                usage(args);
                return false;
            }
            final String key = args[i].substring(0, equalSignPos).trim();
            final String val = args[i].substring(equalSignPos + 1).trim();
            System.out.println(String.format("prop: '%s' -> '%s'", key, val));
            jobParameters.setProperty(key, val);
        }
        return true;
    }

    private static String readJobXmlFilename(String[] args) {

        String jobXmlFilename = null;
        if(args.length == 0 || args[0] == null || args[0].isEmpty()) {
            usage(args);
        }
        else {
            jobXmlFilename = args[0];
        }

        return jobXmlFilename;
    }

    private static void usage(final String[] args) {
        SEBatchLogger.LOGGER.usage(args);
    }

}
