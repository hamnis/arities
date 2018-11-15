package net.hamnaberg.arities;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

public class IOFunctionTest {

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


        SQLFunctionTest.uncheckedException(function.unchecked(), IOException.class);
    }
}
