package com.finance.wallet.controller;

import com.finance.wallet.config.MapperUtil;
import com.finance.wallet.dto.*;
import com.finance.wallet.dto.enums.EntityOperationType;
import com.finance.wallet.model.Account;
import com.finance.wallet.model.Category;
import com.finance.wallet.repository.CategoryRepository;
import com.finance.wallet.service.ExcelService;
import com.finance.wallet.service.WalletHelpService;
import com.finance.wallet.service.WalletService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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


    @PostMapping(value = "/account", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WalletResponse<AccountDto>> accountProcess(@RequestBody WalletRequest<AccountDto> request) {
        try {
            walletHelpService.validateAccount(request.getBody(),request.getEntityOperationType());
            Account account = walletService.accountProcess(request.getBody(), EntityOperationType.valueOf(request.getEntityOperationType()));
            WalletResponse<AccountDto> accountResponse = new WalletResponse<>( mapAccountToAccountDto(account),"Счёт создан успешно");
            return ResponseEntity.status(HttpStatus.OK).body(accountResponse);
        } catch (Exception e) {
            String message = "Ошибка создания счёта " + e;
            WalletResponse<AccountDto> accountResponse = new WalletResponse<>( null,message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(accountResponse);
        }
    }

    @PostMapping(value = "/category", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WalletResponse<CategoryDto>> categoryProcess(@RequestBody WalletRequest<CategoryDto> request) {
        try {
            walletHelpService.validateCategory(request.getBody(),request.getEntityOperationType());
            Category category = walletService.categoryProcess(request.getBody(), EntityOperationType.valueOf(request.getEntityOperationType()));
            WalletResponse<CategoryDto> categoryResponse = new WalletResponse<>( mapCategoryToCategoryDto(category),"Категория создана успешно");
            return ResponseEntity.status(HttpStatus.OK).body(categoryResponse);
        } catch (Exception e) {
            String message = "Ошибка создания счёта " + e;
            WalletResponse<CategoryDto> categoryResponse  = new WalletResponse<>( null,message);
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



    private AccountDto mapAccountToAccountDto(Account account){
      return mapper.map(account, AccountDto.class);
    }

    private CategoryDto mapCategoryToCategoryDto(Category category){
        return mapper.map(category, CategoryDto.class);
    }
}
