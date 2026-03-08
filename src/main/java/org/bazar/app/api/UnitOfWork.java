package org.bazar.app.api;

import java.util.function.Supplier;

public interface UnitOfWork {
    <T> T perform(Supplier<T> action);
}
