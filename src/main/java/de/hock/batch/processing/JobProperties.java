package de.hock.batch.processing;

import org.jberet.runtime.context.JobContextImpl;

import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import java.util.Objects;
import java.util.Properties;

public final class JobProperties {

    @Inject
    private JobContext jobContext;

    public static final String OUTPUT_DIRECTORY = "outputDirectory";
    public static final String THREAD_COUNT = "threadCount";
   // public static final String FILENAME_PATTERN = "filenamePattern";
    public static final String INPUT_DIRECTORY = "inputDirectory";
    public static final String PARTITION_FILENAME = "fileName";
    public static final String LOGGING_DIRECTORY = "loggingDirectory";
    public static final String OUTPUT_FILENAME = "outputFilename";

    public String getOutputFilename() {
        return Objects.isNull(getJobParameter(OUTPUT_FILENAME)) ? getProperty(OUTPUT_FILENAME) : getJobParameter(OUTPUT_FILENAME);
    }

    public String getOutputDirectory() {
        return Objects.isNull(getJobParameter(OUTPUT_DIRECTORY)) ? getProperty(OUTPUT_DIRECTORY) : getJobParameter(OUTPUT_DIRECTORY);
    }

    public String getInputDirectory() {
        return Objects.isNull(getJobParameter(INPUT_DIRECTORY)) ? getProperty(INPUT_DIRECTORY) : getJobParameter(INPUT_DIRECTORY);
    }

    public Integer getThreadCount() {
        String threadCount = Objects.isNull(getJobParameter(THREAD_COUNT)) ? getProperty(THREAD_COUNT) : getJobParameter(THREAD_COUNT);
        threadCount = Objects.isNull(threadCount) ? "5" : threadCount;

        return Integer.parseInt(threadCount);
    }

    private String getProperty(String key) {
        return jobContext.getProperties().getProperty(key);
    }

    private String getJobParameter(String key) {
        if(jobContext instanceof JobContextImpl) {
            Properties jobParameters = ((JobContextImpl) jobContext).getJobParameters();
            return jobParameters.getProperty(key);
        }

        return null;
    }

}
