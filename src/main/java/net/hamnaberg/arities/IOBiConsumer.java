package net.hamnaberg.arities;

import java.io.IOException;
import java.io.Serializable;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface IOBiConsumer<A1, A2> extends Serializable {
    void accept(A1 a1, A2 a2) throws IOException;

    static <A1, A2> IOBiConsumer<A1, A2> fromConsumer(BiConsumer<A1, A2> f) {
        return (a1, a2) -> {
            try {
                f.accept(a1, a2);
            } catch (Exception e) {
                //noinspection ConstantConditions
                if (e instanceof IOException) throw ((IOException)e);
                else throw new IOException(e);
            }
        };
    }

    default BiConsumer<A1, A2> unchecked() {
        return (a1, a2) -> {
            try {
                accept(a1, a2);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    static <A1, A2> IOBiConsumer<A1, A2> untupled(IOConsumer<Tuple2<A1, A2>> f) {
        return (a1, a2) -> f.accept(Tuples.of(a1, a2));
    }

    default IOConsumer<Tuple2<A1, A2>> tupled() {
        return t -> accept(t._1, t._2);
    }


    long serialVersionUID = 1L;
}