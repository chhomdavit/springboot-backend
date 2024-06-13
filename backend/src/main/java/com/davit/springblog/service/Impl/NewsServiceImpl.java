package com.davit.springblog.service.Impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.davit.springblog.dto.NewsDto;
import com.davit.springblog.dto.NewsResponse;
import com.davit.springblog.entity.Category;
import com.davit.springblog.entity.News;
import com.davit.springblog.entity.Users;
import com.davit.springblog.execption.EmptyOrNotNullException;
import com.davit.springblog.execption.ResourceNotFoundException;
import com.davit.springblog.repository.CategoryRepository;
import com.davit.springblog.repository.NewsRepository;
import com.davit.springblog.repository.UserRepository;
import com.davit.springblog.service.FileUploadService;
import com.davit.springblog.service.NewsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService{

    private final NewsRepository newsRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;
    private final ModelMapper modelMapper;

    @Value("${project.upload}")
    private String path;
    @Value("${base.url}")
    private String baseUrl;
    
    @Override
    public NewsDto createNews(NewsDto newsDto, MultipartFile file, Integer categoryId, Integer userId)
            throws IOException {
        if (newsDto.getTitle() == null || newsDto.getTitle().isEmpty()) {
            throw new EmptyOrNotNullException("Post title cannot be null or empty");
        }

        // Save file if provided
        String newFileName = null;
        if (file != null && !file.isEmpty()) {
            newFileName = fileUploadService.saveFile(file, path);
            newsDto.setNewsImage(newFileName);
        }

        // Retrieve category
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + categoryId));

        // Retrieve category
        Users users = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + userId));

        // Map DTO to entity
        News news = modelMapper.map(newsDto, News.class);
        news.setUsers(users);
        news.setCategory(category);

        // Save post
        News saved = newsRepository.save(news);

        // Construct response DTO
        NewsDto response = modelMapper.map(saved, NewsDto.class);
        if (newFileName != null) {
            String newsImageUrl = baseUrl + "/auth/" + newFileName;
            response.setNewsImageUrl(newsImageUrl);
        }
        return response;
    }

    @Override
    public NewsDto updateNews(Integer newsId, NewsDto newsDto, MultipartFile file, Integer categoryId, Integer userId)
            throws IOException {
        Users user = this.userRepository.findById(userId)
	            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
                
        News existingNews = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + newsId));

        if (!existingNews.getUsers().equals(user)) {
                throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        String newSaveFile = null;
        try {
            if (file != null && !file.isEmpty()) {
                if (existingNews.getNewsImage() != null && !existingNews.getNewsImage().isEmpty()) {
                    Files.deleteIfExists(Paths.get(path, existingNews.getNewsImage()));
                }
                newSaveFile = fileUploadService.saveFile(file, path);
            } else {
                newSaveFile = existingNews.getNewsImage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while updating post", e);
        }

        newsDto.setNewsImage(newSaveFile);

        News updated = modelMapper.map(newsDto, News.class);
        updated.setNewsId(existingNews.getNewsId());
        updated.setUsers(user);
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
            updated.setCategory(category);
        } else {
            updated.setCategory(existingNews.getCategory());
        }

        News saved = newsRepository.save(updated);
        NewsDto response = modelMapper.map(saved, NewsDto.class);
        if (newSaveFile != null) {
            String newsImageUrl = baseUrl + "/auth/" + newSaveFile;
            response.setNewsImageUrl(newsImageUrl);
        }
        return response;
    }

    @Override
    public NewsResponse getAllNews(String keyword, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<News> newsPages = newsRepository.findByTitleContainingIgnoreCase(keyword, pageable);

        List<NewsDto> newsDtos = newsPages.getContent().stream().map(news -> {
            NewsDto newsDto = modelMapper.map(news, NewsDto.class);
            return newsDto;
        }).collect(Collectors.toList());

        return new NewsResponse(newsDtos, pageNumber, pageSize, newsPages.getTotalElements(),
        newsPages.getTotalPages(), newsPages.isLast());
    }

    @Override
    public void deleteNews(Integer newsId, Integer userId) throws IOException {
        News existingNews = newsRepository.findById(newsId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + newsId));

        if (!existingNews.getUsers().getId().equals(userId)) {
            throw new ResourceNotFoundException("Post not found with id: " + userId);
        }

        String uploadDirectory = path;
        String photoFileName = existingNews.getNewsImage();

        if (photoFileName != null && !photoFileName.isEmpty()) {
            try {
                Files.deleteIfExists(Paths.get(uploadDirectory, photoFileName));
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException("Failed to delete photo", e);
            }
        }
        newsRepository.deleteById(newsId);
    }

}
