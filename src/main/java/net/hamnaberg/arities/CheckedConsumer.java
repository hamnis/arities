package net.hamnaberg.arities;

import java.util.function.Consumer;

@FunctionalInterface
public interface CheckedConsumer<A> {
    void accept(A input) throws Exception;

    static <A> CheckedConsumer<A> fromConsumer(Consumer<A> f) {
        return f::accept;
    }

    static <A> CheckedConsumer<A> empty() {
        return ignore -> {};
    }

    default Consumer<A> unchecked() {
        return (a) -> {
            try {
                accept(a);
            } catch (Exception e) {
                throw Sneaky.sneakyThrow(e);
            }
        };
    }

    default CheckedFunction<A, Void> toFunction() {
        return a -> {
            accept(a);
            return null;
        };
    }
}
