package net.hamnaberg.arities;

import java.util.function.Supplier;

@FunctionalInterface
public interface CheckedSupplier<A> {
    A get() throws Exception;

    default Supplier<A> unchecked() {
        return () -> {
            try {
                return get();
            } catch (Exception e) {
                throw Sneaky.sneakyThrow(e);
            }
        };
    }

    static <A> CheckedSupplier<A> fromSupplier(Supplier<A> other) {
        return other::get;
    }
}
