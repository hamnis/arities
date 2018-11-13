package net.hamnaberg.arities;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface IOFunction<A, B> extends Serializable {
    B apply(A a) throws IOException;

    static <A, B> IOFunction<A, B> constant(B value) {
        return (ignore) -> value;
    }

    default Function<A, B> unchecked() {
        return (a) -> {
            try {
                return apply(a);
            } catch (IOException e) {
                throw Sneaky.sneakyThrow(e);
            }
        };
    }

    default <V> IOFunction<A, V> andThen(IOFunction<? super B, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return (a) -> after.apply(apply(a));
    }

    default <C> IOFunction<C, B> compose(IOFunction<? super C, ? extends A> before) {
        Objects.requireNonNull(before, "before is null");
        return (v) -> apply(before.apply(v));
    }

    long serialVersionUID = 1L;
}