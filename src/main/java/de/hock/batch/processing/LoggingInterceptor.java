package de.hock.batch.processing;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Tracing
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class LoggingInterceptor {

    private Function<Method, String> paramsLog = m -> Stream.of(m.getParameters()).map(Parameter::getType).map(Class::getName).collect(Collectors.joining(", "));

    private BiFunction<Thread, Method, String> fineEntering = (t, m) -> String.format("[%s: %d]: >>> %s.%s()", t.getName(), t.getId(), m.getDeclaringClass().getSimpleName(), m.getName());
    private QuardFunction<Thread, Method, Object, Duration, String> fineLeaving = (t, m, o, d)
            -> String.format("[%s: %d]: <<< %s.%s() -> %s, time=%s", t.getName(), t.getId(), m.getDeclaringClass().getSimpleName(), m.getName(), String.valueOf(o), d);

    private BiFunction<Thread, Method, String> finerEntering = (t, m) ->
            String.format("[%s: %d]: >>> %s.%s(%s)", t.getName(), t.getId(), m.getDeclaringClass().getSimpleName(), m.getName(), paramsLog.apply(m));
    private QuardFunction<Thread, Method, Object, Duration, String> finerLeaving = (t, m, o, d) ->
            String.format("[%s: %d]: <<< %s.%s(%s) -> %s, time=%s", t.getName(), t.getId(), m.getDeclaringClass().getSimpleName(), m.getName(), paramsLog.apply(m), String.valueOf(o), d);

    private static final Logger logger = Logger.getLogger(LoggingInterceptor.class.getSimpleName());

    @AroundInvoke
    public Object logAround(InvocationContext invocationContext) throws Exception {

        Object result;
        Level traceLevel = getTraceLevel(invocationContext);

        if(logger.isLoggable(traceLevel)) {
            Instant instant = Instant.now();
            Method method = invocationContext.getMethod();
            Thread thread = Thread.currentThread();

//            for(int index = 0; index < method.getParameters().length; index++) {
//                Parameter parameter = method.getParameters()[index];
//                System.out.println("Name: " + parameter.getName());
//                System.out.println("Type: " + parameter.getType().getName());
//                System.out.println("Value: " + invocationContext.getParameters()[index]);
//            }

            if(Level.FINE.equals(traceLevel)) {
                logger.log(traceLevel, () -> fineEntering.apply(thread, method));
            }
            else {
                logger.log(traceLevel, () -> finerEntering.apply(thread, method));
            }

            result = invocationContext.proceed();

            Duration duration = Duration.between(instant, Instant.now());
            if(Level.FINE.equals(traceLevel)) {
                logger.log(traceLevel, () -> fineLeaving.apply(thread, method, result, duration));
            }
            else {
                logger.log(traceLevel, () -> finerLeaving.apply(thread, method, result, duration));
            }
        }
        else {
            result = invocationContext.proceed();
        }

        return result;
    }

    private Level getTraceLevel(InvocationContext invocationContext) {
        Tracing classLevel = invocationContext.getMethod().getDeclaringClass().getAnnotation(Tracing.class);
        Tracing methodLevel = invocationContext.getMethod().getAnnotation(Tracing.class);
        Tracing tracingLevel = Objects.nonNull(methodLevel) ? methodLevel : classLevel;
        Level level = Objects.isNull(tracingLevel) ? Level.FINE : tracingLevel.value().level;

        return level;
    }

}
