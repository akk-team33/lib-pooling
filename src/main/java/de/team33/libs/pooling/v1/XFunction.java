package de.team33.libs.pooling.v1;

/**
 * @see java.util.function.Function
 */
@FunctionalInterface
public interface XFunction<T, R, X extends Exception> {

    /**
     * @see java.util.function.Function#apply(Object)
     */
    R apply(T t) throws X;
}
