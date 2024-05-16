package com.Aswat.services.jwt;

import com.Aswat.Dtos.CategoryDto;
import com.Aswat.entity.Category;
import com.Aswat.reposistories.CategoryRepo;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepo categoryRepo;


    public CategoryServiceImpl(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }
    public Category createCategory(CategoryDto categoryDto){
        Category category= new Category();
        category.setCategory(categoryDto.getCategory());
        return categoryRepo.save(category);

    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }
}
