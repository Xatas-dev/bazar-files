package org.bazar.adapter.persistence.unitofwork;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.bazar.app.api.UnitOfWork;

import java.util.function.Supplier;

@ApplicationScoped
public class HibernateUnitOfWork implements UnitOfWork {
    @Override
    @Transactional
    public <T> T perform(Supplier<T> action) {
        return action.get();
    }
}


