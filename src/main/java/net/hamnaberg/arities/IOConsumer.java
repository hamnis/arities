package net.hamnaberg.arities;

import java.io.IOException;
import java.util.function.Consumer;

@FunctionalInterface
public interface IOConsumer<A> {
    void accept(A input) throws IOException;

    default Consumer<A> unchecked() {
        return (a) -> {
            try {
                accept(a);
            } catch (IOException e) {
                throw Sneaky.sneakyThrow(e);
            }
        };
    }

    default IOFunction<A, Void> toFunction() {
        return a -> {
            accept(a);
            return null;
        };
    }
}
