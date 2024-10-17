package com.example.movie_app.service;

import com.example.movie_app.entity.Blog;
import com.example.movie_app.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;

    public Page<Blog> getBlogs(Boolean status, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("createdAt").descending());
        return blogRepository.findByStatus(status, pageable);
    }

    public Blog getBlogDetails(Integer id, String slug) {
        return blogRepository.findByIdAndSlugAndStatus(id, slug, true)
            .orElse(null);
    }
}
