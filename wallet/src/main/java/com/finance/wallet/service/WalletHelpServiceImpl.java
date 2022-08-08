package com.finance.wallet.service;

import com.finance.wallet.dto.AccountDto;
import com.finance.wallet.dto.CategoryDto;
import com.finance.wallet.dto.OperationDto;
import com.finance.wallet.dto.enums.EntityOperationType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletHelpServiceImpl implements WalletHelpService {

    private static final String NEED_PARAMS = "Отсутсвуют обязательные поля";

    @Autowired
    DataService dataService;

    public void validateOperation(OperationDto operation) {
        if (operation.getCategoryId() == null || operation.getOperationType() == null) {
            throw new IllegalArgumentException(NEED_PARAMS);
        }
    }


    @Override
    public void validateAccount(AccountDto account, String operation) {
        EntityOperationType entityOperationType = EntityOperationType.valueOf(operation);
        switch (entityOperationType) {
            case CREATE:
                if (StringUtils.isEmpty(account.getNameAccount())) {
                    throw new IllegalArgumentException(NEED_PARAMS);
                }
                break;
            case SAVE:
                if (account.getId() == null && StringUtils.isEmpty(account.getNameAccount())) {
                    throw new IllegalArgumentException(NEED_PARAMS);
                }
            case DELETE:
                if (account.getId() == null) {
                    throw new IllegalArgumentException(NEED_PARAMS);
                }
        }

    }

    @Override
    public void validateCategory(CategoryDto category,  String operation) {
        EntityOperationType entityOperationType = EntityOperationType.valueOf(operation);
        switch (entityOperationType) {
            case CREATE:
                if (StringUtils.isEmpty(category.getNameCategory())) {
                    throw new IllegalArgumentException(NEED_PARAMS);
                }
                break;
            case SAVE:
                if (category.getId() == null && StringUtils.isEmpty(category.getNameCategory())) {
                    throw new IllegalArgumentException(NEED_PARAMS);
                }
            case DELETE:
                if (category.getId() == null) {
                    throw new IllegalArgumentException(NEED_PARAMS);
                }
        }
    }

}
