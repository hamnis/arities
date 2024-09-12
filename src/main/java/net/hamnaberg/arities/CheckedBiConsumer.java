package net.hamnaberg.arities;

import java.io.Serializable;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface CheckedBiConsumer<A1, A2> extends Serializable {
    void accept(A1 a1, A2 a2) throws Exception;

    static <A1, A2> CheckedBiConsumer<A1, A2> fromConsumer(BiConsumer<A1, A2> f) {
        return f::accept;
    }

    static <A1, A2> CheckedBiConsumer<A1, A2> empty() {
        return (a1, a2) -> {};
    }

    default BiConsumer<A1, A2> unchecked() {
        return (a1, a2) -> {
            try {
                accept(a1, a2);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    static <A1, A2> CheckedBiConsumer<A1, A2> untupled(CheckedConsumer<Tuple2<A1, A2>> f) {
        return (a1, a2) -> f.accept(Tuples.of(a1, a2));
    }

    default CheckedConsumer<Tuple2<A1, A2>> tupled() {
        return t -> accept(t._1(), t._2());
    }


    long serialVersionUID = 1L;
}
