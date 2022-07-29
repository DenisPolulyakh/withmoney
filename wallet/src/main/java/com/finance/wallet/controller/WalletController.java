package com.finance.wallet.controller;

import com.finance.wallet.model.Category;
import com.finance.wallet.repository.CategoryRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
public class WalletController {


    @Autowired
    CategoryRepository categoryRepository;


    @GetMapping("/test")
    public String test() {
        Category category = new Category();
        category.setName("Одежда");

        categoryRepository.save(category);
        return new Gson().toJson(1);
    }

}
