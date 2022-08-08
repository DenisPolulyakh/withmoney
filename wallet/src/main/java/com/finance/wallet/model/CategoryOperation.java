package com.finance.wallet.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * Сущность приход
 * Таблица <b>category_operation</b>
 */
@Entity
@Table(name = "category_operation")
@Getter
@Setter
public class CategoryOperation extends GenericEntity<Long>{

    /**
     * Ссылка на категорию
     */
    @ManyToOne
    @JoinColumn(name="category_id", nullable=false)
    private Category category;

    /**
     * Тип операции
     */
    @Column(name = "operation_type")
    private String operationType;

    /**
     * Сумма
     */
    @Column(name = "amount", precision = 15, scale = 2)
    private BigDecimal amount;

    /**
     * Дата
     */
    @Column(name = "operation_time", nullable = false)
    private LocalDateTime operationTime;

    /**
     * Комментарий
     */
    @Column(name = "comment")
    private String comment;


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
