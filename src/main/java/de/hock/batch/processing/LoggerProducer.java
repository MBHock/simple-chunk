package de.hock.batch.processing;

import javax.batch.api.BatchProperty;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.Objects;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static de.hock.batch.processing.JobProperties.LOGGING_DIRECTORY;

/**
 * @author <a href="mailto:Mojammal.Hock@gmail.com">Mojammal Hock</a>
 */
public class LoggerProducer {

    @BatchProperty(name = LOGGING_DIRECTORY)
    private String logDirecotry;

    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint) {
        Logger logger = LogManager.getLogManager().getLogger(injectionPoint.getMember().getDeclaringClass().getName());
        if(Objects.isNull(logger)) {
            logger = Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
            LogManager.getLogManager().addLogger(logger);
        }

        return logger;
    }

}
