package net.hamnaberg.arities;

import java.sql.SQLException;
import java.util.function.Consumer;

@FunctionalInterface
public interface SQLConsumer<A> {
    void accept(A input) throws SQLException;

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
