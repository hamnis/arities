package net.hamnaberg.arities;

import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class SQLConsumerTest {

    @Test
    public void normalOp() {
        SQLConsumer<Void> function = (a) -> {
        };

        try {
            function.accept(null);
        } catch (SQLException e) {
            Assert.fail();
        }
    }

    @Test
    public void catchException() {
        SQLConsumer<Void> function = (a) -> {
            throw new SQLException("Nope");
        };

        try {
            function.accept(null);
            Assert.fail();
        } catch (SQLException e) {
            Assert.assertEquals("Nope", e.getMessage());
        }
    }

    @Test
    public void catchExceptionGenerated() {
        SQLConsumer3<Void, Void, Void> function = (a, b, c) -> {
            throw new SQLException("Nope");
        };

        try {
            function.accept(null, null, null);
            function.tupled().accept(Tuples.of(null, null, null));
            Assert.fail();
        } catch (SQLException e) {
            Assert.assertEquals("Nope", e.getMessage());
        }
    }

    @Test
    public void uncheckedExceptionShouldNotBeWrapped() {
        SQLConsumer<Void> function = (a) -> {
            throw new SQLException("Nope");
        };


        SQLFunctionTest.uncheckedException(function.toFunction().unchecked(), SQLException.class);
    }
}
