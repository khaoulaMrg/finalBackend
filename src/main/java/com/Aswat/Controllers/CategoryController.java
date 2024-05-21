package com.Aswat.Controllers;


import com.Aswat.Dtos.CategoryDto;
import com.Aswat.entity.Category;
import com.Aswat.reposistories.CategoryRepo;
import com.Aswat.services.jwt.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;


@RestController
@RequestMapping("/api/customer")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("cat")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDto categoryDto) {
        Category category = categoryService.createCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }


@GetMapping("cats")
    public ResponseEntity<List<Category>>getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
}
}
