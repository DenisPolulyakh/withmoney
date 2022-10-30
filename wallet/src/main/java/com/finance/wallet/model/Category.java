package com.finance.wallet.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Сущность категория
 * Таблица <b>category</b>
 */
@Entity
@Table(name = "category")
@Getter
@Setter
public class Category  extends GenericEntity<Long>{


    @Column(name = "name_category")
    private String nameCategory;

    /**
     * Ссылка на счёт
     */
    @ManyToOne
    @JoinColumn(name="account_id")
    private Account account;

    /**
     * Статус категории
     */
    @Column(name = "state")
    private String state;


    /**
     * Описание
     */
    @Column(name = "description")
    private String description;

    /**
     * Приход
     */
    @OneToMany(mappedBy="category", fetch = FetchType.EAGER)
    private List<CategoryOperation> debit;
    /**
     * Резерв
     */
    @OneToMany(mappedBy="category", fetch = FetchType.EAGER)
    private List<CategoryOperation> reserve;

    /**
     * Расход
     */
    @OneToMany(mappedBy="category", fetch = FetchType.EAGER)
    private List<CategoryOperation> credit;

    /**
     * Общая сумма
     */
    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    /**
     * Время создания записи
     */
    @Column(name = "create_time", nullable = false)
    @CreationTimestamp
    private LocalDateTime createTime;


    /**
     * Время обновления записи
     */
    @Column(name = "update_time", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updateTime;
}
