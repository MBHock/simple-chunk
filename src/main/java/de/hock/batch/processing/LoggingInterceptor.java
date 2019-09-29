package de.hock.batch.processing;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.logging.Logger;

import static java.util.logging.Level.FINER;
import static java.util.logging.Level.FINEST;

@Tracing
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class LoggingInterceptor {

    private BiFunction<Thread, Method, String> fineEntering = (t, m) -> String.format("[%s: %d]: >>> %s.%s().", t.getName(), t.getId(), m.getDeclaringClass().getSimpleName(), m.getName());
    private TriFunction<Thread, Method, Object, String> fineLeaving = (t, m, o) -> String.format("[%s: %d]: <<< %s.%s() -> %s.", t.getName(), t.getId(), m.getDeclaringClass().getSimpleName(), m.getName(), String.valueOf(o));

    private BiFunction<Thread, Method, String> finerEntering = (t, m) -> String.format("[%s: %d]: >>> %s.%s(%s).", t.getName(), t.getId(), m.getDeclaringClass().getSimpleName(), m.getName(), Arrays.deepToString(m.getParameters()));
    private TriFunction<Thread, Method, Object, String> finerLeaving = (t, m, o) -> String.format("[%s: %d]: <<< %s.%s(%s) -> %s.", t.getName(), t.getId(), m.getDeclaringClass().getSimpleName(), m.getName(), String.valueOf(o));

    private static final Logger logger = Logger.getAnonymousLogger();

    @AroundInvoke
    public Object logAround(InvocationContext invocationContext) throws Exception {

        if(logger.isLoggable(FINER) || logger.isLoggable(FINEST)) {
            Method method = invocationContext.getMethod();
            Thread thread = Thread.currentThread();

            logger.log(FINER, () -> fineEntering.apply(thread, method));
            if(logger.isLoggable(FINEST)) {
                logger.log(FINEST, () -> finerEntering.apply(thread, method));
            }
        }

        Object result = invocationContext.proceed();

        if(logger.isLoggable(FINER) || logger.isLoggable(FINEST)) {
            Method method = invocationContext.getMethod();
            Thread thread = Thread.currentThread();

            logger.log(FINER, () -> fineLeaving.apply(thread, method, result));
            if(logger.isLoggable(FINEST)) {
                logger.log(FINEST, () -> finerLeaving.apply(thread, method, result));
            }
        }

        return result;
    }

}
