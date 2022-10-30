package com.finance.wallet.service;

import com.finance.wallet.dto.AccountDto;
import com.finance.wallet.dto.CategoryDto;
import com.finance.wallet.dto.OperationDto;
import com.finance.wallet.dto.enums.EntityOperationType;
import com.finance.wallet.dto.enums.OperationType;
import com.finance.wallet.dto.enums.State;
import com.finance.wallet.model.*;
import com.finance.wallet.repository.AccountPageRepository;
import com.finance.wallet.repository.AccountRepository;
import com.finance.wallet.repository.CategoryOperationRepository;
import com.finance.wallet.repository.CategoryPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {

    public static final String NO_CATEGORY = "Не найдена  категория";

    @Autowired
    private DataService dataService;

    @Autowired
    private CategoryOperationRepository categoryOperationRepository;

    @Autowired
    private CategoryPageRepository categoryRepository;

    @Autowired
    private AccountPageRepository accountRepository;

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

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = dataService.selectFrom(QCategory.category).fetch();
        return categories;
    }

    @Override
    public Page<Category> getPagingCategories(Pageable pageable) {
        return categoryRepository.findAllByState("ACTIVE", pageable);
    }

    @Override
    public List<Account> getPagingAccounts() {
        List<Account> accounts = dataService.selectFrom(QAccount.account).fetch();
        return accounts;
    }

    @Override
    public Page<Account> getPagingAccounts(Pageable pageable) {
        return accountRepository.findAllByState("ACTIVE", pageable);
    }

    @Override
    public Category getCategory(Long id) {
        return dataService.selectFromWhere(QCategory.category, QCategory.class, c -> c.id.eq(id)).fetchFirst();
    }

    @Override
    public Category saveCategory(Long id, Category category) {
        category.setState(State.ACTIVE.name());
        if (id != null) {
            Category categoryOld = dataService.selectFromWhere(QCategory.category, QCategory.class, c -> c.id.eq(id)).fetchFirst();
            category.setCreateTime(categoryOld.getCreateTime());
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category deleteCategory(Long id) {
        Category category = dataService.selectFromWhere(QCategory.category, QCategory.class, c -> c.id.eq(id)).fetchFirst();
        if (category != null) {
            category.setState(State.DELETE.name());
            return categoryRepository.save(category);
        }
        throw new IllegalArgumentException("Категория не найдена");
    }

    @Override
    public Account getAccount(Long id) {
        return dataService.selectFromWhere(QAccount.account, QAccount.class, c -> c.id.eq(id)).fetchFirst();
    }

    @Override
    public Account saveAccount(Long id, Account account) {
        account.setState(State.ACTIVE.name());
        if (id != null) {
            Account accountOld = dataService.selectFromWhere(QAccount.account, QAccount.class, c -> c.id.eq(id)).fetchFirst();
            account.setCreateTime(accountOld.getCreateTime());
        }
        return accountRepository.save(account);
    }

    @Override
    public Account deleteAccount(Long id) {
        Account account = dataService.selectFromWhere(QAccount.account, QAccount.class, c -> c.id.eq(id)).fetchFirst();

        if (account != null) {
            account.setState(State.DELETE.name());
            Account deleteAccount = accountRepository.save(account);
            //В категориях удаляем этот счет
            List<Category> categories = dataService.selectFromWhere(QCategory.category, QCategory.class, c -> c.account.id.eq(deleteAccount.getId())).fetch();
            if (categories != null) {
                categories.stream().forEach(category -> {
                    category.setAccount(null);
                    categoryRepository.save(category);
                });
            }
            return deleteAccount;
        }
        throw new IllegalArgumentException("Счёт не найден");
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
