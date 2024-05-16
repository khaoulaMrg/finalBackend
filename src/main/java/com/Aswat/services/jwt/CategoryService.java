package com.Aswat.services.jwt;

import com.Aswat.Dtos.CategoryDto;
import com.Aswat.entity.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(CategoryDto categoryDto);

    List<Category> getAllCategories();

}
