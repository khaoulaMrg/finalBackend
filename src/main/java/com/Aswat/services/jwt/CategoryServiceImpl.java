package com.Aswat.services.jwt;

import com.Aswat.Dtos.CategoryDto;
import com.Aswat.entity.Category;
import com.Aswat.reposistories.CategoryRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepo categoryRepo;
    private final PostService postService;


    public CategoryServiceImpl(CategoryRepo categoryRepo, PostService postService) {
        this.categoryRepo = categoryRepo;
        this.postService = postService;
    }
    public Category createCategory(CategoryDto categoryDto) {
        Category category= new Category();
        category.setCategory(categoryDto.getCategory());
        return categoryRepo.save(category);

    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }
    @Scheduled(fixedRate = 60000) // Vérifie toutes les minutes
    public void archiveAndRemoveExpiredPosts() {
        postService.archiveAndRemoveExpiredPosts();
    }
}
