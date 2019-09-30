package de.hock.batch.processing;

public interface QuardFunction<T, U, O, D, R> {

    R apply(T t, U u, O o, D d);

}
