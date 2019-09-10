package de.hock.batch.processing;

import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;

public class JobAndStepPropertyReader {

    @Inject
    private JobContext jobContext;

    public String getProperty(String key) {
        return jobContext.getProperties().getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return jobContext.getProperties().getProperty(key, defaultValue);
    }

    public Integer getPropertyAsInteger(String key) {
        return Integer.parseInt(getProperty(key));
    }

}
