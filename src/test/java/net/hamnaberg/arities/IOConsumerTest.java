package net.hamnaberg.arities;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class IOConsumerTest {

    @Test
    public void normalOp() {
        IOConsumer<Void> function = (a) -> {
        };

        try {
            function.accept(null);
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void catchException() {
        IOConsumer<Void> function = (a) -> {
            throw new IOException("Nope");
        };

        try {
            function.accept(null);
            Assert.fail();
        } catch (IOException e) {
            Assert.assertEquals("Nope", e.getMessage());
        }
    }

    @Test
    public void catchExceptionGenerated() {
        IOConsumer3<Void, Void, Void> function = (a, b, c) -> {
            throw new IOException("Nope");
        };

        try {
            function.accept(null, null, null);
            function.tupled().accept(Tuples.of(null, null, null));
            Assert.fail();
        } catch (IOException e) {
            Assert.assertEquals("Nope", e.getMessage());
        }
    }

    @Test
    public void uncheckedExceptionShouldNotBeWrapped() {
        IOConsumer<Void> function = (a) -> {
            throw new IOException("Nope");
        };


        SQLFunctionTest.uncheckedException(function.toFunction().unchecked(), IOException.class);
    }
}
