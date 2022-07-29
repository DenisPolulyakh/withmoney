package com.finance.wallet.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Сущность категория
 * Таблица <b>category</b>
 */
@Entity
@Table(name = "category")
@Getter
@Setter
public class Category  extends GenericEntity<Long>{
    private String name;
}
