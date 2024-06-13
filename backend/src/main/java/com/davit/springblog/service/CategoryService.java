package com.davit.springblog.service;

import java.io.IOException;
import java.util.List;

import com.davit.springblog.dto.CategoryDto;
import com.davit.springblog.dto.CategoryResponse;




public interface CategoryService {

        CategoryDto createCategory (CategoryDto categoryDto, Integer userId) throws IOException;

        CategoryDto updateCategory (Integer categoryId, CategoryDto categoryDto, Integer userId) throws IOException;

        void  deleteCategory (Integer categoryId, Integer userId);

        CategoryDto getCategory (Integer categoryId);

        List<CategoryDto> getCategories ();

        CategoryResponse getPaginationAndSearch(String keyword, Integer pageNumber, Integer pageSize);
}
