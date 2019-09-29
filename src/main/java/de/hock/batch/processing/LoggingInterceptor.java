package de.hock.batch.processing;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.FINER;
import static java.util.logging.Level.FINEST;

@Tracing
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class LoggingInterceptor {

    private BiFunction<Thread, Method, String> fineEntering = (t, m) -> String.format("[%s: %d]: >>> %s.%s()", t.getName(), t.getId(), m.getDeclaringClass().getSimpleName(), m.getName());
    private TriFunction<Thread, Method, Object, String> fineLeaving = (t, m, o) -> String.format("[%s: %d]: <<< %s.%s() -> %s", t.getName(), t.getId(), m.getDeclaringClass().getSimpleName(), m.getName(), String.valueOf(o));

    private BiFunction<Thread, Method, String> finerEntering = (t, m) -> String.format("[%s: %d]: >>> %s.%s(%s)", t.getName(), t.getId(), m.getDeclaringClass().getSimpleName(), m.getName(), Arrays.deepToString(m.getParameters()));
    private TriFunction<Thread, Method, Object, String> finerLeaving = (t, m, o) -> String.format("[%s: %d]: <<< %s.%s(%s) -> %s", t.getName(), t.getId(), m.getDeclaringClass().getSimpleName(), m.getName(), Arrays.deepToString(m.getParameters()), String.valueOf(o));

    private static final Logger logger = Logger.getAnonymousLogger();

    @AroundInvoke
    public Object logAround(InvocationContext invocationContext) throws Exception {

        Object result;
        Level traceLevel = getTraceLevel(invocationContext);

        if(logger.isLoggable(traceLevel)) {
            Method method = invocationContext.getMethod();
            Thread thread = Thread.currentThread();

            if(Objects.equals(FINER, traceLevel) || Objects.equals(FINEST, traceLevel)) {
                logger.log(traceLevel, () -> finerEntering.apply(thread, method));
            }
            else {
                logger.log(traceLevel, () -> fineEntering.apply(thread, method));
            }

            result = invocationContext.proceed();

            if(Objects.equals(FINER, traceLevel) || Objects.equals(FINEST, traceLevel)) {
                logger.log(traceLevel, () -> finerLeaving.apply(thread, method, result));
            }
            else {
                logger.log(traceLevel, () -> fineLeaving.apply(thread, method, result));
            }
        }
        else {
            result = invocationContext.proceed();
        }

        return result;
    }

    private Level getTraceLevel(InvocationContext invocationContext) {
        TracingLevel classLevel = invocationContext.getMethod().getDeclaringClass().getAnnotation(TracingLevel.class);
        TracingLevel methodLevel = invocationContext.getMethod().getAnnotation(TracingLevel.class);
        TracingLevel tracingLevel = Objects.nonNull(methodLevel) ? methodLevel : classLevel;
        Level level = Objects.isNull(tracingLevel) ? Level.parse("FINE") : Level.parse(tracingLevel.level());

        return level;
    }

}
