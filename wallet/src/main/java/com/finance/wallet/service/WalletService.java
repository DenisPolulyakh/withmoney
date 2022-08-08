package com.finance.wallet.service;

import com.finance.wallet.dto.AccountDto;
import com.finance.wallet.dto.CategoryDto;
import com.finance.wallet.dto.OperationDto;
import com.finance.wallet.dto.enums.EntityOperationType;
import com.finance.wallet.model.Account;
import com.finance.wallet.model.Category;


public interface WalletService {
    /**
     * Проведение операции в кошельке
     * @param operationDto - данные операции
     */
    void processOperation(OperationDto operationDto);

    /**
     * Сздание/Изменение/Удаление  аккаунта
     * @param accountDto - данные счёта
     * @return
     */
    Account accountProcess(AccountDto accountDto, EntityOperationType operationType);

    /**
     * Сздание категории
     * @param categoryDto - данные категории
     * @return
     */
    Category categoryProcess(CategoryDto categoryDto, EntityOperationType operationType);


}
