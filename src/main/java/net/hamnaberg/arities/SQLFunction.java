package net.hamnaberg.arities;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface SQLFunction<A, B> extends Serializable {
    B apply(A a) throws SQLException;

    static <A, B> SQLFunction<A, B> constant(B value) {
        return (ignore) -> value;
    }

    static <A> SQLFunction<A, A> identity() {
        return a -> a;
    }

    static <A, B> SQLFunction<A, B> fromFunction(Function<A, B> f) {
        return a -> Sneaky.getOrThrowE(() -> f.apply(a), SQLException.class, SQLException::new);
    }

    default Function<A, B> unchecked() {
        return (a) -> {
            try {
                return apply(a);
            } catch (SQLException e) {
                throw Sneaky.sneakyThrow(e);
            }
        };
    }

    default <C> SQLFunction<A, C> flatMap(SQLFunction<B, SQLFunction<A, C>> fn) {
        return a -> fn.apply(apply(a)).apply(a);
    }

    default <C> SQLFunction<A, C> map(SQLFunction<B, C> fn) {
        return andThen(fn);
    }

    default <V> SQLFunction<A, V> andThen(SQLFunction<? super B, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return (a) -> after.apply(apply(a));
    }

    default <C> SQLFunction<C, B> compose(SQLFunction<? super C, ? extends A> before) {
        Objects.requireNonNull(before, "before is null");
        return (v) -> apply(before.apply(v));
    }

    long serialVersionUID = 1L;
}
