package net.hamnaberg.arities;

import java.io.IOException;

public interface IOSupplier<A> {
    A get() throws IOException;
}
