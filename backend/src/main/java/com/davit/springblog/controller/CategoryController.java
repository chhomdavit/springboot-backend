package com.davit.springblog.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.davit.springblog.constant.AppConstants;
import com.davit.springblog.dto.CategoryDto;
import com.davit.springblog.dto.CategoryResponse;
import com.davit.springblog.entity.Users;
import com.davit.springblog.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/admin/category")
    public ResponseEntity<CategoryDto> createCategory(
        @RequestBody CategoryDto categoryDto,
        Authentication authentication
        ) throws IOException {
        Users users = (Users) authentication.getPrincipal();
        CategoryDto createdCategory = categoryService.createCategory(categoryDto, users.getId());
        return ResponseEntity.ok().body(createdCategory);
    }

    @PutMapping("/admin/category/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable(value = "categoryId") Integer categoryId,
            @RequestBody CategoryDto categoryDto,
            Authentication authentication
            ) throws IOException {
        Users users = (Users) authentication.getPrincipal();
        CategoryDto updatedCategory = categoryService.updateCategory(categoryId, categoryDto, users.getId());
        return ResponseEntity.ok().body(updatedCategory);
    }

    @GetMapping("/auth/categories")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        List<CategoryDto> catDto = categoryService.getCategories();
        return ResponseEntity.ok(catDto);
    }

    @DeleteMapping("/admin/category/{categoryId}")
    public ResponseEntity<String> deleteCategory(
        @PathVariable Integer categoryId,
        Authentication authentication
        ) {
        Users users = (Users) authentication.getPrincipal();
        categoryService.deleteCategory(categoryId, users.getId());
        return ResponseEntity.ok("Category deleted successfully");
    }

    @GetMapping("/admin/category/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Integer categoryId) {
        CategoryDto categoryDto = categoryService.getCategory(categoryId);
        return ResponseEntity.ok(categoryDto);
    }


    @GetMapping("/adminuser/category")
    public ResponseEntity<CategoryResponse> getPaginationAndSearch(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize) {
        CategoryResponse categoryResponse = categoryService.getPaginationAndSearch(keyword, pageNumber, pageSize);
        return ResponseEntity.ok().body(categoryResponse);
    }
}
