package net.hamnaberg.arities;

import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class SQLFunctionTest {

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
    public void uncheckedExceptionShouldNotBeWrapped() {
        SQLFunction<Void, Void> function = (a) -> {
            throw new SQLException("Nope");
        };


        try {
            function.unchecked().apply(null);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals("Nope", e.getMessage());
            Assert.assertTrue(e instanceof SQLException);
        }
    }
}
