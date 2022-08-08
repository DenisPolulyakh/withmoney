package com.finance.wallet.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Сущность счёт
 * Таблица <b>account</b>
 */
@Entity
@Table(name = "account")
@Getter
@Setter
public class Account extends GenericEntity<Long>{
    /**
     * Название счёта
     */
    @Column(name = "name_account")
    private String nameAccount;

    /**
     * Категория
     */
    @OneToMany(mappedBy="account",fetch = FetchType.EAGER)
    private Set<Category> categories;

    /**
     * Статус счёта
     */
    @Column(name = "state")
    private String state;

    /**
     * Валюта
     */
    @Column(name = "currency")
    private String currency;

    /**
     * Описание
     */
    @Column(name = "description")
    private String description;

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
