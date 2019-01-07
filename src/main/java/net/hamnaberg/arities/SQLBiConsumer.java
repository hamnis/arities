package net.hamnaberg.arities;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@FunctionalInterface
public interface SQLBiConsumer<A1, A2> extends Serializable {
    void accept(A1 a1, A2 a2) throws SQLException;

    static <A1, A2> SQLBiConsumer<A1, A2> fromConsumer(BiConsumer<A1, A2> f) {
        return (a1, a2) -> {
            try {
                f.accept(a1, a2);
            } catch (Exception e) {
                //noinspection ConstantConditions
                if (e instanceof SQLException) throw ((SQLException)e);
                else throw new SQLException(e);
            }
        };
    }

    static <A1, A2> SQLBiConsumer<A1, A2> empty() {
        return (a1, a2) -> {};
    }

    default BiConsumer<A1, A2> unchecked() {
        return (a1, a2) -> {
            try {
                accept(a1, a2);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
    }

    static <A1, A2> SQLBiConsumer<A1, A2> untupled(SQLConsumer<Tuple2<A1, A2>> f) {
        return (a1, a2) -> f.accept(Tuples.of(a1, a2));
    }

    default SQLConsumer<Tuple2<A1, A2>> tupled() {
        return t -> accept(t._1, t._2);
    }


    long serialVersionUID = 1L;
}