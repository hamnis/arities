package net.hamnaberg.arities;

import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Random;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class SQLFunctionTest {

    @Test
    public void identity() {
        try {
            assertEquals(1, SQLFunction.identity().apply(1));
        } catch (SQLException e) {
            Assert.fail();
        }
    }

    @Test
    public void constant() {
        try {
            assertEquals(2, SQLFunction.<Integer, Integer>constant(2).apply(new Random().nextInt()).intValue());
        } catch (SQLException e) {
            Assert.fail();
        }
    }

    @Test
    public void normalOp() {
        SQLFunction<Integer, Integer> function = (a) -> a + 1;

        try {
            assertEquals(2, function.apply(1).intValue());
        } catch (SQLException e) {
            Assert.fail();
        }
    }

    @Test
    public void catchException() {
        SQLFunction<Void, Void> function = (a) -> {
            throw new SQLException("Nope");
        };

        try {
            function.apply(null);
            Assert.fail();
        } catch (SQLException e) {
            Assert.assertEquals("Nope", e.getMessage());
        }
    }

    @Test
    public void catchExceptionGenerated() {
        SQLFunction3<Void, Void, Void, Void> function = (a, b, c) -> {
            throw new SQLException("Nope");
        };

        try {
            function.apply(null, null, null);
            function.tupled().apply(Tuples.of(null, null, null));
            Assert.fail();
        } catch (SQLException e) {
            Assert.assertEquals("Nope", e.getMessage());
        }
    }

    @Test
    public void uncheckedExceptionShouldNotBeWrapped() {
        SQLFunction<Void, Void> function = (a) -> {
            throw new SQLException("Nope");
        };


        uncheckedException(function.unchecked(), SQLException.class);
    }

    static <E extends Exception> void uncheckedException(Function<Void, Void> unchecked, Class<E> exceptionType) {
        try {
            unchecked.apply(null);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals("Nope", e.getMessage());
            Assert.assertTrue(exceptionType.isInstance(e));
        }
    }

    @Test
    public void composeAndThen() {
        SQLFunction<Integer, Void> function1 = (a) -> null;
        SQLFunction<Void, Integer> function2 = (a) -> 1;
        SQLFunction<Void, Void> compose = function1.compose(function2);
        SQLFunction<Void, Void> andThen = function2.andThen(function1);
        try {
            compose.apply(null);
            andThen.apply(null);
        } catch (SQLException e) {
            Assert.fail();
        }
    }

}
