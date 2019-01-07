package net.hamnaberg.arities;

import java.sql.SQLException;

public interface SQLSupplier<A> {
    A get() throws SQLException;
}
