package com.finance.wallet.service;

import com.finance.wallet.model.Category;
import com.finance.wallet.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ExcelService {
    @Autowired
    private ExcelHelpService helpService;
    @Autowired
    private CategoryRepository repository;

    public void loadFile(MultipartFile file) throws IllegalArgumentException {
        try {
            List<Category> categoryList = helpService.importToCategories(file.getInputStream());
            for(Category c:categoryList){
                repository.save(c);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка парсинга файла");
        }

    }
}
