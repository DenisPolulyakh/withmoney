package com.finance.wallet.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * DTO операция
 */

@Getter
@Setter
public class OperationDto {


    /**
     * Ссылка на категорию
     */
    private Long categoryId;

    /**
     * Тип операции
     */
    private String operationType;

    /**
     * Сумма
     */
    private BigDecimal amount;

    /**
     * Комментарий
     */
    private String comment;

}
