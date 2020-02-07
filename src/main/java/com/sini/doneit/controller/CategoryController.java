package com.sini.doneit.controller;

import com.sini.doneit.model.Category;
import com.sini.doneit.repository.CategoryJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
public class CategoryController {

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @GetMapping(path = "/all-categories")
    public List<Category> getAllCategories(){
        return this.categoryJpaRepository.findAll();
    }
}
