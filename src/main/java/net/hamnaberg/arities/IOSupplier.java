package net.hamnaberg.arities;

import java.io.IOException;
import java.util.function.Supplier;

public interface IOSupplier<A> {
    A get() throws IOException;

    default Supplier<A> unchecked() {
        return () -> {
            try {
                return get();
            } catch (IOException e) {
                throw Sneaky.sneakyThrow(e);
            }
        };
    }

    static <A> IOSupplier<A> fromSupplier(Supplier<A> other) {
        return () -> Sneaky.getOrThrowE(other::get, IOException.class, IOException::new);
    }
}
