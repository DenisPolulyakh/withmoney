package com.finance.wallet.model.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 * Created by kurdyukovpv on 18.03.2018.
 * <p>
 * Реализация Pageable без разбивки на реальные страницы
 * Логика разбивки на страницы происходит во вне
 * Выборка по Skip/Take (offset/limit)
 */
public class Range implements Pageable, Serializable {

    private int skip;
    private int take;
    private Sort sort;

    private Range(Integer skip, Integer take, Sort sort) {
        this.skip = skip;
        this.take = take;
        this.sort = sort;
    }

    public static Range of(Integer skip, Integer take) {
        return new Range(skip, take, Sort.unsorted());
    }

    public static Range of(Integer skip, Integer take, Sort sort) {
        return new Range(skip, take, sort);
    }

    public static Range of(Sort sort) {
        return new Range(0, Integer.MAX_VALUE, sort);
    }

    @Override
    public int getPageNumber() {
        return 0;
    }

    /**
     * В данном контексте размер страницы - это количество элементов которые нужно выбрать (limit ?)
     *
     * @return количество элементов для выборки
     */
    @Override
    public int getPageSize() {
        return take;
    }

    /**
     * Количество элементов которые нужно пропустить
     *
     * @return количество элементов для пропуска
     */
    @Override
    public long getOffset() {
        return skip;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return null;
    }

    @Override
    public Pageable previousOrFirst() {
        return null;
    }

    @Override
    public Pageable first() {
        return null;
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }
}
