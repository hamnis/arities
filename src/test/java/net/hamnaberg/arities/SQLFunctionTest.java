package net.hamnaberg.arities;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.function.Function;

public class SQLFunctionTest {

    @Test
    public void catchException() {
        IOFunction<Void, Void> function = (a) -> {
            throw new IOException("Nope");
        };

        try {
            function.apply(null);
            Assert.fail();
        } catch (IOException e) {
            Assert.assertEquals("Nope", e.getMessage());
        }
    }

    @Test
    public void uncheckedExceptionShouldNotBeWrapped() {
        IOFunction<Void, Void> function = (a) -> {
            throw new IOException("Nope");
        };


        uncheckedException(function.unchecked());
    }

    static void uncheckedException(Function<Void, Void> unchecked) {
        try {
            unchecked.apply(null);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals("Nope", e.getMessage());
            Assert.assertTrue(e instanceof SQLException);
        }
    }
}
