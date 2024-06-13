package com.davit.springblog.service.Impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.davit.springblog.dto.CategoryDto;
import com.davit.springblog.dto.CategoryResponse;
import com.davit.springblog.dto.PostDto;
import com.davit.springblog.entity.Category;
import com.davit.springblog.entity.Users;
import com.davit.springblog.execption.AlreadyExistsException;
import com.davit.springblog.execption.EmptyOrNotNullException;
import com.davit.springblog.execption.ResourceNotFoundException;
import com.davit.springblog.repository.CategoryRepository;
import com.davit.springblog.repository.UserRepository;
import com.davit.springblog.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto, Integer userId) throws IOException {

        if (categoryDto.getTitle() == null || categoryDto.getTitle().isEmpty()) {
            throw new EmptyOrNotNullException("Category title cannot be null or empty");
        } else if (categoryRepository.existsByTitle(categoryDto.getTitle())) {
            throw new AlreadyExistsException("Category title already exists");
        }

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("userId not found with id: " + userId));

        Category category = modelMapper.map(categoryDto, Category.class);
        category.setUsers(user);
        Category saved = categoryRepository.save(category);
        CategoryDto response = modelMapper.map(saved, CategoryDto.class);
        return response;
    }

    @Override
    public CategoryDto updateCategory(Integer categoryId, CategoryDto categoryDto, Integer userId) throws IOException {

        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("userId not found with id: " + userId));

        if (categoryDto.getTitle() != null && !categoryDto.getTitle().isEmpty()) {
            existingCategory.setTitle(categoryDto.getTitle());
            existingCategory.setDescription(categoryDto.getDescription());
            existingCategory.getUsers().equals(user);
        }

        Category saved = categoryRepository.save(existingCategory);
        CategoryDto response = modelMapper.map(saved, CategoryDto.class);
        return response;
    }

    @Override
    public void deleteCategory(Integer categoryId, Integer userId) {

        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("userId not found with id: " + userId));

        if (!existingCategory.getUsers().equals(user)) {
            throw new ResourceNotFoundException("userId not found with id: " + userId);
        }

        this.categoryRepository.delete(existingCategory);
    }

    @Override
    public CategoryDto getCategory(Integer categoryId) {

        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        return modelMapper.map(existingCategory, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getCategories() {

        List<Category> catList = this.categoryRepository.findAll();
        List<CategoryDto> updatedDto = catList.stream().map(cat -> {
            CategoryDto categoryDto = this.modelMapper.map(cat, CategoryDto.class);
            categoryDto.setPostCount(cat.getPosts().size()); // Set the post count

            // Map post entities to post DTOs
            List<PostDto> postDtos = cat.getPosts().stream()
                    .map(post -> this.modelMapper.map(post, PostDto.class))
                    .collect(Collectors.toList());
            categoryDto.setPostData(postDtos); // Set the post data

            return categoryDto;
        }).collect(Collectors.toList());

        return updatedDto;
    }

    @Override
    public CategoryResponse getPaginationAndSearch(String keyword, Integer pageNumber, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Category> categoryPages = keyword == null || keyword.trim().isEmpty()
                ? categoryRepository.findAll(pageable)
                : categoryRepository.findByTitleContainingIgnoreCase(keyword, pageable);

        List<CategoryDto> categoryDtos = categoryPages.getContent().stream()
                .map(category -> {
                    CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
                    categoryDto.setPostCount(category.getPosts().size());

                    List<PostDto> postDtos = category.getPosts().stream()
                            .map(post -> modelMapper.map(post, PostDto.class))
                            .collect(Collectors.toList());
                    categoryDto.setPostData(postDtos);

                    return categoryDto;
                })
                .collect(Collectors.toList());

        return new CategoryResponse(categoryDtos, pageNumber, pageSize, categoryPages.getTotalElements(),
                categoryPages.getTotalPages(), categoryPages.isLast());
    }

}
