package de.team33.libs.pooling.v1;

/**
 * @see java.util.function.Consumer
 */
@FunctionalInterface
public interface XConsumer<T, X extends Exception> {

    /**
     * @see java.util.function.Consumer#accept(Object)
     */
    void accept(T t) throws X;
}
