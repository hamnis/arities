package net.hamnaberg.arities;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

@FunctionalInterface
public interface SQLBiFunction<A1, A2, B> extends Serializable {
    B apply(A1 a1, A2 a2) throws SQLException;

    static <A1, A2, B> SQLBiFunction<A1, A2, B> constant(B value) {
        return (ignore1, ignore2) -> value;
    }

    default BiFunction<A1, A2, B> unchecked() {
        return (a1, a2) -> {
            try {
                return apply(a1, a2);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
    }

    static <A1, A2, B> SQLBiFunction<A1, A2, B> untupled(Function<Tuple2<A1, A2>, B> f) {
        return (a1, a2) -> f.apply(Tuples.of(a1, a2));
    }

    default SQLFunction<Tuple2<A1, A2>, B> tupled() {
        return t -> apply(t._1, t._2);
    }

    default SQLFunction<A1, SQLFunction<A2, B>> curried() {
        return a1 -> a2 -> apply(a1 , a2);
    }

    default <V> SQLBiFunction<A1, A2, V> andThen(SQLFunction<? super B, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return (a1, a2) -> after.apply(apply(a1, a2));
    }

    long serialVersionUID = 1L;
}