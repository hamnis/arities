package net.hamnaberg.arities;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class IOFunctionTest {

    @Test
    public void identity() {
        try {
            assertEquals(1, IOFunction.identity().apply(1));
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void constant() {
        try {
            assertEquals(2, IOFunction.<Integer, Integer>constant(2).apply(new Random().nextInt()).intValue());
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void normalOp() {
        IOFunction<Integer, Integer> function = (a) -> a + 1;

        try {
            assertEquals(2, function.apply(1).intValue());
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void catchException() {
        IOFunction<Void, Void> function = (a) -> {
            throw new IOException("Nope");
        };

        try {
            function.apply(null);
            Assert.fail();
        } catch (IOException e) {
            assertEquals("Nope", e.getMessage());
        }
    }

    @Test
    public void catchExceptionGenerated() {
        IOFunction3<Void, Void, Void, Void> function = (a, b, c) -> {
            throw new IOException("Nope");
        };

        try {
            function.apply(null, null, null);
            function.tupled().apply(Tuples.of(null, null, null));
            Assert.fail();
        } catch (IOException e) {
            assertEquals("Nope", e.getMessage());
        }
    }

    @Test
    public void uncheckedExceptionShouldNotBeWrapped() {
        IOFunction<Void, Void> function = (a) -> {
            throw new IOException("Nope");
        };


        SQLFunctionTest.uncheckedException(function.unchecked(), IOException.class);
    }

    @Test
    public void composeAndThen() {
        IOFunction<Integer, Void> function1 = (a) -> null;
        IOFunction<Void, Integer> function2 = (a) -> 1;
        IOFunction<Void, Void> compose = function1.compose(function2);
        IOFunction<Void, Void> andThen = function2.andThen(function1);
        try {
            compose.apply(null);
            andThen.apply(null);
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void sneaky() {
        IOFunction<Void, Void> function = (a) -> {
            throw new IOException("Nope");
        };
        IOFunction<Void, Void> throwingAgain = IOFunction.fromFunction(function.unchecked());
        try {
            throwingAgain.apply(null);
            Assert.fail();
        } catch (IOException e) {
            assertEquals("Nope", e.getMessage());
        }

    }
}
