package com.finance.wallet.dto;

import com.finance.wallet.model.Category;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;


/**
 * DTO счёт
 */

@Getter
@Setter
public class AccountDto {

    /**
     * id Account
     */
    private Long id;

    /**
     * Название счёта
     */
    private String nameAccount;


    /**
     * Статус счёта
     */
    private String currency;

    /**
     * Статус счёта
     */
    private String state;

    /**
     * Описание
     */
    private String description;
}
