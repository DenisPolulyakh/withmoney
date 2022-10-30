package com.finance.wallet.service;

import com.finance.wallet.dto.AccountDto;
import com.finance.wallet.dto.CategoryDto;
import com.finance.wallet.dto.OperationDto;
import com.finance.wallet.dto.enums.EntityOperationType;
import com.finance.wallet.model.Account;
import com.finance.wallet.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface WalletService {
    /**
     * Проведение операции в кошельке
     *
     * @param operationDto - данные операции
     */
    void processOperation(OperationDto operationDto);

    /**
     * Сздание/Изменение/Удаление  аккаунта
     *
     * @param accountDto - данные счёта
     * @return
     */
    Account accountProcess(AccountDto accountDto, EntityOperationType operationType);

    /**
     * Сздание категории
     *
     * @param categoryDto - данные категории
     * @return
     */
    Category categoryProcess(CategoryDto categoryDto, EntityOperationType operationType);

    /**
     * Получение всех категорий
     */

    List<Category> getAllCategories();

    /**
     * Получение всех категорий
     * постранично
     */

    Page<Category> getPagingCategories(Pageable pageable);


    /**
     * Получение всех счетов
     */

    List<Account> getPagingAccounts();

    /**
     * Получение всех счетов
     * постранично
     */

    Page<Account> getPagingAccounts(Pageable pageable);

    /**
     * Получение категории
     *
     * @param id
     * @return
     */
    Category getCategory(Long id);

    /**
     * Сохраннение категории
     *
     * @param id       - id категории
     * @param category - данные категории
     * @return
     */
    Category saveCategory(Long id, Category category);


    /**
     * Удаление категории
     *
     * @param id - id категории
     * @return
     */
    Category deleteCategory(Long id);


    /**
     * Получение счёта
     *
     * @param id
     * @return
     */
    Account getAccount(Long id);


    /**
     * Сохраннение счёта
     *
     * @param id      - id счёта
     * @param account - данные счёта
     * @return
     */
    Account saveAccount(Long id, Account account);


    /**
     * Удаление счёта
     *
     * @param id - id счёта
     * @return
     */
    Account deleteAccount(Long id);

}
