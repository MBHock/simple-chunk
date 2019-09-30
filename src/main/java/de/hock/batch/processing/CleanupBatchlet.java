package de.hock.batch.processing;

import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.BatchStatus;
import javax.inject.Inject;
import javax.inject.Named;

@Tracing
@Named(value = "CleanupBatchlet")
public class CleanupBatchlet extends AbstractBatchlet {

    @Inject
    private AsychronousWriter asychronousWriter;

    @Override
    public String process() throws Exception {
        asychronousWriter.close();
        return BatchStatus.COMPLETED.name();
    }

}
