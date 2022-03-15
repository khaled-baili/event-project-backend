package com.eventproject.service;

import com.eventproject.model.Blog;
import com.eventproject.repository.BlogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService {
    @Autowired private BlogRepo blogRepo;

    public Blog saveBlog(Blog blog) {
        return blogRepo.save(blog);
    }

    public void deleteBlog(Blog blog) {
        blogRepo.delete(blog);
    }

    public void deleteAllBlogs() {
        blogRepo.deleteAll();
    }

    public void deleteBlogById(long id) {
        blogRepo.deleteById(id);
    }

    public List<Blog> getAllBlog() {
        return blogRepo.findAll();
    }

    public List<Blog> getBlogsByTitle(String title) {
        return blogRepo.findAllByTitle(title);
    }

    public Optional<Blog> titleExist(String title) {
        return blogRepo.findByTitle(title);
    }
}
