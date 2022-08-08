package com.finance.wallet.service;

import com.finance.wallet.dto.AccountDto;
import com.finance.wallet.dto.CategoryDto;
import com.finance.wallet.dto.OperationDto;
import com.finance.wallet.dto.enums.State;
import com.finance.wallet.model.Account;
import com.finance.wallet.model.Category;
import com.finance.wallet.model.QAccount;
import com.finance.wallet.model.QCategory;
import com.finance.wallet.repository.AccountRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public interface WalletHelpService {
    /**
     * Проверка на обязательные параметры операции
     * @param operation - данные операции
     */
    void validateOperation(OperationDto operation);

    void validateAccount(AccountDto account, String entityOperation);

    void validateCategory(CategoryDto category, String entityOperation);
}
