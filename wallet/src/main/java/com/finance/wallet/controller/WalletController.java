package com.finance.wallet.controller;

import com.finance.wallet.dto.*;
import com.finance.wallet.dto.enums.EntityOperationType;
import com.finance.wallet.model.Account;
import com.finance.wallet.model.Category;
import com.finance.wallet.model.dao.Range;
import com.finance.wallet.model.dao.SortUtils;
import com.finance.wallet.repository.CategoryRepository;
import com.finance.wallet.service.ExcelService;
import com.finance.wallet.service.WalletHelpService;
import com.finance.wallet.service.WalletService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletHelpService walletHelpService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private ModelMapper mapper;


    @GetMapping("/test")
    public String test() {
        Category category = new Category();
        category.setNameCategory("Одежда");

        categoryRepository.save(category);
        return new Gson().toJson(1);
    }

    @GetMapping(path = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<CategoryDto>> getCategories(@RequestParam(value = "_start", defaultValue = "0") Integer start, @RequestParam(value = "_end", defaultValue = "10") Integer end, @RequestParam(value = "_sort", defaultValue = "id") String sortBy, @RequestParam(value = "_order", defaultValue = "ASC") String order) {


        Sort.Direction direction = Sort.Direction.ASC;
        if ("DESC".equalsIgnoreCase(order)) {
            direction = Sort.Direction.DESC;
            sortBy = "-" + sortBy;
        }
        Pageable pageable = Range.of(start, end - start, Sort.by(SortUtils.getOrder(sortBy, direction)));
        Page<Category> categories = walletService.getPagingCategories(pageable);
        List<CategoryDto> categoryDtos = mapCategoryListToCategoryDtoList(categories.getContent());
        return ResponseEntity.ok()
                .header("X-Total-Count", "" + categories.getTotalElements())
                .body(categoryDtos);
    }


    @GetMapping(path = "/categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<CategoryDto> getCategories(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok().body(mapCategoryToCategoryDto(walletService.getCategory(id)));
    }


    @PutMapping(path = "/categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<CategoryDto> saveCategory(@PathVariable(value = "id") Long id, @RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok().body(mapCategoryToCategoryDto(walletService.saveCategory(id, mapCategoryDtoToCategory(categoryDto))));
    }


    @DeleteMapping(path = "/categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok().body(mapCategoryToCategoryDto(walletService.deleteCategory(id)));
    }


    @PostMapping(path = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        Category category = walletService.saveCategory(null, mapCategoryDtoToCategory(categoryDto));
        return ResponseEntity.ok().body(mapCategoryToCategoryDto(category));
    }

    @GetMapping(path = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<AccountDto>> getAccounts(@RequestParam(value = "_start", defaultValue = "0") Integer start, @RequestParam(value = "_end", defaultValue = "10") Integer end, @RequestParam(value = "_sort", defaultValue = "id") String sortBy, @RequestParam(value = "_order", defaultValue = "ASC") String order) {
        Sort.Direction direction = Sort.Direction.ASC;
        if ("DESC".equalsIgnoreCase(order)) {
            direction = Sort.Direction.DESC;
            sortBy = "-" + sortBy;
        }
        Pageable pageable = Range.of(start, end - start, Sort.by(SortUtils.getOrder(sortBy, direction)));
        Page<Account> accounts = walletService.getPagingAccounts(pageable);
        List<AccountDto> accountDtos = mapAccountListToAccountDtoList(accounts.getContent());
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(accounts.getTotalElements()))
                .body(accountDtos);
    }

    @PostMapping(path = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
        Account account = walletService.saveAccount(null, mapAccountDtoToAccount(accountDto));
        return ResponseEntity.ok().body(mapAccountToAccountDto(account));
    }

    @GetMapping(path = "/accounts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<AccountDto> getAccount(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok().body(mapAccountToAccountDto(walletService.getAccount(id)));
    }


    @PutMapping(path = "/accounts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<AccountDto> saveAccount(@PathVariable(value = "id") Long id, @RequestBody AccountDto accountDto) {
        return ResponseEntity.ok().body(mapAccountToAccountDto(walletService.saveAccount(id, mapAccountDtoToAccount(accountDto))));
    }


    @DeleteMapping(path = "/accounts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<AccountDto> deleteAccount(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok().body(mapAccountToAccountDto(walletService.deleteAccount(id)));
    }


    @PostMapping(value = "/account", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WalletResponse<AccountDto>> accountProcess(@RequestBody WalletRequest<AccountDto> request) {
        try {
            walletHelpService.validateAccount(request.getBody(), request.getEntityOperationType());
            Account account = walletService.accountProcess(request.getBody(), EntityOperationType.valueOf(request.getEntityOperationType()));
            WalletResponse<AccountDto> accountResponse = new WalletResponse<>(mapAccountToAccountDto(account), "Счёт создан успешно");
            return ResponseEntity.status(HttpStatus.OK).body(accountResponse);
        } catch (Exception e) {
            String message = "Ошибка создания счёта " + e;
            WalletResponse<AccountDto> accountResponse = new WalletResponse<>(null, message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(accountResponse);
        }
    }

    @PostMapping(value = "/category", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WalletResponse<CategoryDto>> categoryProcess(@RequestBody WalletRequest<CategoryDto> request) {
        try {

            walletHelpService.validateCategory(request.getBody(), request.getEntityOperationType());
            Category category = walletService.categoryProcess(request.getBody(), EntityOperationType.valueOf(request.getEntityOperationType()));
            WalletResponse<CategoryDto> categoryResponse = new WalletResponse<>(mapCategoryToCategoryDto(category), "Категория создана успешно");
            return ResponseEntity.status(HttpStatus.OK).body(categoryResponse);
        } catch (Exception e) {
            String message = "Ошибка создания счёта " + e;
            WalletResponse<CategoryDto> categoryResponse = new WalletResponse<>(null, message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(categoryResponse);
        }
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseMessage> importFile(@RequestParam("file") MultipartFile file) {
        try {
            excelService.loadFile(file);
            String message = "Imported the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            String message = "Error import file " + e;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

    }

    @PostMapping(value = "/operation", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> operation(@RequestBody OperationDto operation) {
        try {
            walletHelpService.validateOperation(operation);
            walletService.processOperation(operation);
        } catch (Exception e) {
            String message = "Ошибка проведения операции: " + e;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Операция успешно проведена"));
    }


    private AccountDto mapAccountToAccountDto(Account account) {
        return mapper.map(account, AccountDto.class);
    }

    private Account mapAccountDtoToAccount(AccountDto account) {
        return mapper.map(account, Account.class);
    }

    private CategoryDto mapCategoryToCategoryDto(Category category) {
        return mapper.map(category, CategoryDto.class);
    }


    private Category mapCategoryDtoToCategory(CategoryDto categoryDto) {
        return mapper.map(categoryDto, Category.class);
    }

    private List<CategoryDto> mapCategoryListToCategoryDtoList(List<Category> categoryList) {
        return mapper.map(categoryList, new TypeToken<List<CategoryDto>>() {
        }.getType());
    }

    private List<AccountDto> mapAccountListToAccountDtoList(List<Account> accountList) {
        return mapper.map(accountList, new TypeToken<List<AccountDto>>() {
        }.getType());
    }
}
