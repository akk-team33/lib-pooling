package de.team33.libs.pooling.v1;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Pool<E> {

    private final Supplier<? extends E> newElement;
    private final Queue<E> backing;

    public Pool(final Supplier<? extends E> newElement) {
        this.newElement = newElement;
        backing = new ConcurrentLinkedQueue<>();
    }

    @SuppressWarnings("LambdaUnfriendlyMethodOverload")
    private static <E> XFunction<E, Void, RuntimeException> toXFunction(final Consumer<E> consumer) {
        return element -> {
            consumer.accept(element);
            return null;
        };
    }

    @SuppressWarnings("LambdaUnfriendlyMethodOverload")
    private static <E, X extends Exception> XFunction<E, Void, X> toXFunction(final XConsumer<E, X> consumer) {
        return element -> {
            consumer.accept(element);
            return null;
        };
    }

    private static <E, R> XFunction<E, R, RuntimeException> toXFunction(final Function<E, R> function) {
        return function::apply;
    }

    public final void run(final Consumer<E> consumer) {
        getEx(toXFunction(consumer));
    }

    public final <X extends Exception> void runEx(final XConsumer<E, X> xConsumer) throws X {
        getEx(toXFunction(xConsumer));
    }

    public final <R> R get(final Function<E, R> function) {
        return getEx(toXFunction(function));
    }

    public final <R, X extends Exception> R getEx(final XFunction<E, R, X> xFunction) throws X {
        final E element = Optional.ofNullable(backing.poll())
                                  .orElseGet(newElement);
        try {
            return xFunction.apply(element);
        } finally {
            backing.add(element);
        }
    }

    public final int size() {
        return backing.size();
    }
}
