package de.hock.batch.processing;

import org.jberet.runtime.JobExecutionImpl;
import org.jberet.se._private.SEBatchLogger;
import org.jberet.se._private.SEBatchMessages;

import javax.batch.operations.BatchRuntimeException;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:Mojammal.Hock@gmail.com">Mojammal Hock</a>
 */
public class JobStarter {

    private static final Logger logger = Logger.getLogger(JobStarter.class.getSimpleName());
    public static final String LOGGING_PROPERTIES = "logging.properties";

    static {
        try {
            LogManager logManager = LogManager.getLogManager();
            logManager.reset();
            logManager.readConfiguration(getResourceAsStream(LOGGING_PROPERTIES));
        }
        catch(IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Error while reading logging property from {0}", LOGGING_PROPERTIES);
            throw new RuntimeException(e);
        }
    }

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(final String[] args) throws BatchRuntimeException {

        final String jobXML = readJobXmlFilename(args);
        final Properties jobParameters = new Properties();
        if(addValidArgumentToProps(args, jobParameters)) {
            startJob(jobXML, BatchRuntime.getJobOperator(), jobParameters);
        }
    }


    private static InputStream getResourceAsStream(String propertyFileName) {
        InputStream is = LoggerProducer.class.getClassLoader().getResourceAsStream(propertyFileName);
        Objects.requireNonNull(is, () -> String.format("The property file %s expected in the %s directory, but is missing", propertyFileName, "src/main/resources"));
        return is;
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
            logger.log(Level.INFO, "Commandline arg: {0} = {1}", new Object[]{key, val});
            jobParameters.setProperty(key, val);
        }
        return true;
    }

    private static String readJobXmlFilename(String[] args) {
        logger.log(Level.INFO, "Job filename is: {0}", args[0]);

        if(args.length == 0 || args[0] == null || args[0].isEmpty()) {
            usage(args);
        }

        return args[0];
    }

    private static void usage(final String[] args) {
        SEBatchLogger.LOGGER.usage(args);
    }

}
