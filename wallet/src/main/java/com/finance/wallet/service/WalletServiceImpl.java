package com.finance.wallet.service;

import com.finance.wallet.dto.AccountDto;
import com.finance.wallet.dto.CategoryDto;
import com.finance.wallet.dto.OperationDto;
import com.finance.wallet.dto.enums.EntityOperationType;
import com.finance.wallet.dto.enums.OperationType;
import com.finance.wallet.dto.enums.State;
import com.finance.wallet.model.*;
import com.finance.wallet.repository.AccountRepository;
import com.finance.wallet.repository.CategoryOperationRepository;
import com.finance.wallet.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class WalletServiceImpl implements WalletService {

    public static final String NO_CATEGORY = "Не найдена  категория";

    @Autowired
    private DataService dataService;

    @Autowired
    private CategoryOperationRepository categoryOperationRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void processOperation(OperationDto operationDto) {
        Category category = dataService.selectFromWhere(QCategory.category, QCategory.class, c -> c.id.eq(operationDto.getCategoryId())).fetchFirst();
        if (category != null) {
            calculateAmount(category, operationDto);
        } else {
            throw new IllegalArgumentException(NO_CATEGORY);
        }
    }

    @Override
    public Account accountProcess(AccountDto accountDto, EntityOperationType operationType) {
        switch (operationType) {
            case SAVE:
                Account oldAccount = dataService.selectFromWhere(QAccount.account, QAccount.class, a -> a.id.eq(accountDto.getId())).fetchFirst();
                if (oldAccount != null) {
                    return accountRepository.save(mapAccountDtoToAccount(accountDto, oldAccount));
                }
            case CREATE:
                Account account = new Account();
                return accountRepository.save(mapAccountDtoToAccount(accountDto, account));
            case DELETE:
                Account accountDelete = dataService.selectFromWhere(QAccount.account, QAccount.class, a -> a.id.eq(accountDto.getId())).fetchFirst();
                if (accountDelete != null) {
                    accountDelete.setState(State.DELETE.name());
                    return accountRepository.save(accountDelete);
                }

        }
        throw new IllegalArgumentException("Неверная операция");
    }

    @Override
    public Category categoryProcess(CategoryDto categoryDto, EntityOperationType operationType) {
        switch (operationType) {
            case SAVE:
                Category oldCategory = dataService.selectFromWhere(QCategory.category, QCategory.class, a -> a.id.eq(categoryDto.getId())).fetchFirst();
                if (oldCategory != null) {
                    return categoryRepository.save(mapCategoryDtoToCategory(categoryDto, oldCategory));
                }
            case CREATE:
                Category category = new Category();
                return categoryRepository.save(mapCategoryDtoToCategory(categoryDto, category));
            case DELETE:
                Category categoryDelete = dataService.selectFromWhere(QCategory.category, QCategory.class, a -> a.id.eq(categoryDto.getId())).fetchFirst();
                if (categoryDelete != null) {
                    categoryDelete.setState(State.DELETE.name());
                    return categoryRepository.save(categoryDelete);
                }

        }
        throw new IllegalArgumentException("Неверная операция");
    }

    private void calculateAmount(Category category, OperationDto operationDto) {
        OperationType operationType = OperationType.valueOf(operationDto.getOperationType());
        CategoryOperation categoryOperation = createCategoryOperation(category, operationDto);
        BigDecimal totalAmount = null;
        switch (operationType) {
            case DEBIT:
                totalAmount = category.getTotalAmount().add(categoryOperation.getAmount());
                category.setTotalAmount(totalAmount);
                break;
            case CREDIT:
                totalAmount = category.getTotalAmount().subtract(categoryOperation.getAmount());
                category.setTotalAmount(totalAmount);
                break;
        }
        if (totalAmount != null) {
            categoryRepository.save(category);
        }
    }

    private CategoryOperation createCategoryOperation(Category category, OperationDto operationDto) {
        CategoryOperation categoryOperation = new CategoryOperation();
        categoryOperation.setCategory(category);
        categoryOperation.setOperationTime(LocalDateTime.now());
        categoryOperation.setComment(operationDto.getComment());
        categoryOperation.setAmount(operationDto.getAmount());
        categoryOperation.setOperationType(operationDto.getOperationType());
        return categoryOperationRepository.save(categoryOperation);
    }


    private Account mapAccountDtoToAccount(AccountDto accountDto, Account account) {
        account.setNameAccount(accountDto.getNameAccount());
        account.setState(accountDto.getState());
        account.setCurrency(accountDto.getCurrency());
        account.setDescription(accountDto.getDescription());
        return account;
    }

    private Category mapCategoryDtoToCategory(CategoryDto categoryDto, Category category) {
        category.setNameCategory(categoryDto.getNameCategory());
        if (categoryDto.getAccountId() != null) {
            Account account = dataService.selectFromWhere(QAccount.account, QAccount.class, a -> a.id.eq(categoryDto.getAccountId())).fetchFirst();
            category.setAccount(account);
        } else {
            category.setAccount(null);
        }
        category.setTotalAmount(categoryDto.getTotalAmount() == null ? new BigDecimal(0) : categoryDto.getTotalAmount());
        category.setState(categoryDto.getState() == null ? State.ACTIVE.name() : categoryDto.getState());
        return category;
    }
}
