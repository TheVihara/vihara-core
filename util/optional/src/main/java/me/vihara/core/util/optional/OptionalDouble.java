package me.vihara.core.util.optional;

import lombok.NonNull;

import java.util.NoSuchElementException;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;

/**
 * Custom wrapper for optional double
 */
public final class OptionalDouble {

    private static final OptionalDouble EMPTY = new OptionalDouble();

    private final boolean isPresent;
    private final double value;

    private OptionalDouble() {
        this.isPresent = false;
        this.value = 0;
    }

    public static OptionalDouble empty() {
        return EMPTY;
    }

    private OptionalDouble(final double value) {
        this.isPresent = true;
        this.value = value;
    }

    public static OptionalDouble of(final double value) {
        return new OptionalDouble(value);
    }

    public double getAsDouble() {
        if (!isPresent) {
            throw new NoSuchElementException("No value present");
        }

        return value;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public boolean isEmpty() {
        return !isPresent;
    }

    public void ifPresent(final @NonNull DoubleConsumer action) {
        if (isPresent) {
            action.accept(value);
        }
    }

    public void ifPresentOrElse(final @NonNull DoubleConsumer action,
                                final @NonNull Runnable emptyAction) {
        if (isPresent) {
            action.accept(value);
        } else {
            emptyAction.run();
        }
    }

    public DoubleStream stream() {
        if (isPresent) {
            return DoubleStream.of(value);
        } else {
            return DoubleStream.empty();
        }
    }

    public double orElse(final double other) {
        return isPresent ? value : other;
    }

    public double orElseGet(final @NonNull DoubleSupplier supplier) {
        return isPresent ? value : supplier.getAsDouble();
    }

    public double orElseThrow() {
        if (!isPresent) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    public<X extends Throwable> double orElseThrow(final @NonNull Supplier<? extends X> exceptionSupplier) throws X {
        if (isPresent) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof OptionalDouble)) {
            return false;
        }

        OptionalDouble other = (OptionalDouble) obj;

        return (isPresent && other.isPresent)
                ? value == other.value
                : isPresent == other.isPresent;
    }

    @Override
    public int hashCode() {
        return isPresent ? Double.hashCode(value) : 0;
    }

    @Override
    public String toString() {
        return isPresent
                ? String.format("OptionalDouble[%s]", value)
                : "OptionalDouble.empty";
    }
}
