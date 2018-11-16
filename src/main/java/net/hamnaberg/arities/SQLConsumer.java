package net.hamnaberg.arities;

import java.sql.SQLException;
import java.util.function.Consumer;

@FunctionalInterface
public interface SQLConsumer<A> {
    void accept(A input) throws SQLException;

    static <A> SQLConsumer<A> fromConsumer(Consumer<A> f) {
        return a -> {
            try {
                f.accept(a);
            } catch (Exception e) {
                //noinspection ConstantConditions
                if (e instanceof SQLException) throw ((SQLException)e);
                else throw new SQLException(e);
            }
        };
    }

    default Consumer<A> unchecked() {
        return (a) -> {
            try {
                accept(a);
            } catch (SQLException e) {
                throw Sneaky.sneakyThrow(e);
            }
        };
    }

    default SQLFunction<A, Void> toFunction() {
        return a -> {
            accept(a);
            return null;
        };
    }
}
