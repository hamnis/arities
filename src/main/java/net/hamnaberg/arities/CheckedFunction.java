package net.hamnaberg.arities;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface CheckedFunction<A, B> extends Serializable {
    B apply(A a) throws Exception;

    static <A, B> CheckedFunction<A, B> constant(B value) {
        return (ignore) -> value;
    }

    static <A> CheckedFunction<A, A> identity() {
        return a -> a;
    }

    static <A, B> CheckedFunction<A, B> fromFunction(Function<A, B> f) {
        return f::apply;
    }

    default Function<A, B> unchecked() {
        return (a) -> {
            try {
                return apply(a);
            } catch (Exception e) {
                throw Sneaky.sneakyThrow(e);
            }
        };
    }

    default <C> CheckedFunction<A, C> flatMap(CheckedFunction<B, CheckedFunction<A, C>> fn) {
        return a -> fn.apply(apply(a)).apply(a);
    }

    default <C> CheckedFunction<A, C> map(CheckedFunction<B, C> fn) {
        return andThen(fn);
    }

    default <V> CheckedFunction<A, V> andThen(CheckedFunction<? super B, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return (a) -> after.apply(apply(a));
    }

    default <C> CheckedFunction<C, B> compose(CheckedFunction<? super C, ? extends A> before) {
        Objects.requireNonNull(before, "before is null");
        return (v) -> apply(before.apply(v));
    }

    long serialVersionUID = 1L;
}
