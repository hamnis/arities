package net.hamnaberg.arities;

import java.sql.SQLException;
import java.util.function.Supplier;

public interface SQLSupplier<A> {
    A get() throws SQLException;

    default Supplier<A> unchecked() {
        return () -> {
            try {
                return get();
            } catch (SQLException e) {
                throw Sneaky.sneakyThrow(e);
            }
        };
    }

    static <A> SQLSupplier<A> fromSupplier(Supplier<A> other) {
        return () -> Sneaky.getOrThrowE(other::get, SQLException.class, SQLException::new);
    }
}
