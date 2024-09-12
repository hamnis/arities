package net.hamnaberg.arities;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiFunction;

@FunctionalInterface
public interface CheckedBiFunction<A1, A2, B> extends Serializable {
    B apply(A1 a1, A2 a2) throws Exception;

    static <A1, A2, B> CheckedBiFunction<A1, A2, B> constant(B value) {
        return (ignore1, ignore2) -> value;
    }

    static <A1, A2, B> CheckedBiFunction<A1, A2, B> fromFunction(BiFunction<A1, A2, B> f) {
        return f::apply;
    }

    default BiFunction<A1, A2, B> unchecked() {
        return (a1, a2) -> {
            try {
                return apply(a1, a2);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    static <A1, A2, B> CheckedBiFunction<A1, A2, B> untupled(CheckedFunction<Tuple2<A1, A2>, B> f) {
        return (a1, a2) -> f.apply(Tuples.of(a1, a2));
    }

    default CheckedFunction<Tuple2<A1, A2>, B> tupled() {
        return t -> apply(t._1(), t._2());
    }

    default CheckedFunction<A1, CheckedFunction<A2, B>> curried() {
        return a1 -> a2 -> apply(a1 , a2);
    }

    default <V> CheckedBiFunction<A1, A2, V> andThen(CheckedFunction<? super B, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return (a1, a2) -> after.apply(apply(a1, a2));
    }

    long serialVersionUID = 1L;
}
