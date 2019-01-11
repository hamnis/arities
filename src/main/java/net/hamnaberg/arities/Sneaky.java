package net.hamnaberg.arities;

import java.util.concurrent.Callable;
import java.util.function.Function;

final class Sneaky {
    private Sneaky() {
    }

    @SuppressWarnings("unchecked")
    static <E extends Throwable> RuntimeException sneakyThrow(Throwable e) throws E {
        throw (E) e;
    }

    static <B, E extends Exception> B getOrThrowE(Callable<B> e, Class<E> type, Function<Exception, E> constructor) throws E {
        try {
            return e.call();
        } catch (Exception e1) {
            if (type.isInstance(e1)) {
                throw type.cast(e1);
            }
            else throw constructor.apply(e1);
        }
    }

    static <E extends Exception> void runOrThrowE(Runnable run, Class<E> type, Function<Exception, E> constructor) throws E {
        try {
            run.run();
        } catch (Exception e1) {
            if (type.isInstance(e1)) {
                throw type.cast(e1);
            }
            else throw constructor.apply(e1);
        }
    }
}
