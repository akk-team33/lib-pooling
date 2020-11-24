package de.team33.test.pooling.v1;

import de.team33.libs.pooling.v1.Pool;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static de.team33.libs.testing.v1.Attempts.*;
import static org.junit.Assert.*;

public class PoolTest {

    private final Pool<Random> randomPool = new Pool<>(Random::new);
    private final List<Integer> shelf = Collections.synchronizedList(new LinkedList<>());

    @Test
    public final void run() {
        tryParallel(100, () -> randomPool.run(random -> shelf.add(random.nextInt())));
        assertEquals(100, shelf.size());
        assertNotEquals(0, randomPool.size());
    }

    @Test
    public final void runEx() throws IOException {
        tryParallel(100, () -> randomPool.runEx(random -> shelf.add(nextIntEx(random))));
        assertEquals(100, shelf.size());
        assertNotEquals(0, randomPool.size());
    }

    @Test
    public final void get() {
        tryParallel(100, () -> {
            final int sample = randomPool.get(Random::nextInt);
            shelf.add(sample);
        });
        assertEquals(100, shelf.size());
        assertNotEquals(0, randomPool.size());
    }

    @Test
    public final void getEx() throws IOException {
        tryParallel(100, () -> {
            final int sample = randomPool.getEx(PoolTest::nextIntEx);
            shelf.add(sample);
        });
        assertEquals(100, shelf.size());
        assertNotEquals(0, randomPool.size());
    }

    private static int nextIntEx(final Random random) throws IOException {
        return Optional.of(random.nextInt())
                       .orElseThrow(IOException::new);
    }
}
