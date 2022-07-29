package com.finance.wallet.service;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.function.Function;

@Service
public class DataService {

    @PersistenceContext
    EntityManager em;

    public <T, U> JPAQuery<T> selectFromWhere(EntityPath<T> entity, Class<U> cls, Function<U, Predicate> where) {
        return selectFrom(entity).where(where.apply((U) entity));
    }

    public <T> JPAQuery<T> selectFrom(EntityPath<T> entity) {
        return select(entity).from(entity);
    }

    public <T> JPAQuery<T> select(Expression<T> select) {
        return new JPAQuery<>(em).select(select);
    }

}
