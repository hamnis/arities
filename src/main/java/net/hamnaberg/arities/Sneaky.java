package net.hamnaberg.arities;

final class Sneaky {
    private Sneaky() {
    }

    @SuppressWarnings("unchecked")
    static <E extends Throwable> RuntimeException sneakyThrow(Throwable e) throws E {
        throw (E) e;
    }
}
