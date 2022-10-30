package com.finance.wallet.dto;

import com.finance.wallet.model.GenericEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO категория
 */

@Getter
@Setter
public class CategoryDto extends GenericEntity<Long> {


    /**
     * id Category
     */
    private Long id;

    /**
     * Наименование категории
     */
    private String nameCategory;

    /**
     *  Ссылка на счёт
     */

    private Long accountId;

   /* *//**
     * Нименование счета
     *//*
    private String nameAccount;*/

    /**
     * Общая сумма категории
     */
    private BigDecimal totalAmount;

    /**
     * Статус
     */
    private String state;

    /**
     * Описание
     */
    private String description;

}
