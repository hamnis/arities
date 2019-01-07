package net.hamnaberg.arities;

import java.io.IOException;
import java.util.function.Consumer;

@FunctionalInterface
public interface IOConsumer<A> {
    void accept(A input) throws IOException;

    static <A> IOConsumer<A> fromConsumer(Consumer<A> f) {
        return a -> {
            try {
                f.accept(a);
            } catch (Exception e) {
                //noinspection ConstantConditions
                if (e instanceof IOException) throw ((IOException)e);
                else throw new IOException(e);
            }
        };
    }

    static <A> IOConsumer<A> empty() {
        return ignore -> {};
    }

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
