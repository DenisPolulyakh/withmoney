package com.finance.wallet.service;

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

@Service
public class ExcelHelpService {

    public static final String SHEET = "Categories";

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    DataService dataService;

    List<Category> importToCategories(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<Category> categories = new ArrayList<>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                Iterator<Cell> cellsInRow = currentRow.iterator();
                Category category = new Category();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 0:
                            category.setNameCategory(currentCell.getStringCellValue());
                            break;
                        case 1:
                            category.setTotalAmount(new BigDecimal(currentCell.getNumericCellValue()));
                            break;
                        case 2:
                            category.setAccount(createAccount(currentCell.getStringCellValue()));
                            break;
                    }
                    cellIdx++;
                }
                category.setState(State.ACTIVE.name());
                addCategory(category, categories);
            }
            workbook.close();
            return categories;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    private void addCategory(Category categoryNew, List<Category> categories) {
        Category categoryOld = dataService.selectFromWhere(QCategory.category, QCategory.class, a -> a.nameCategory.eq(categoryNew.getNameCategory()).and(a.totalAmount.eq(categoryNew.getTotalAmount())).and(a.account.id
        .eq(categoryNew.getAccount().getId()))).fetchFirst();
        if (categoryOld != null) {
            return;
        }
        categories.add(categoryNew);
    }

    private Account createAccount(String searchName) {
        Account account = dataService.selectFromWhere(QAccount.account, QAccount.class, a -> a.nameAccount.eq(searchName).and(a.state.eq(State.ACTIVE.name()))).fetchFirst();
        if (account == null) {
            account = new Account();
            account.setNameAccount(searchName);
            account.setState(State.ACTIVE.name());
            return accountRepository.save(account);

        }
        return account;

    }
}
