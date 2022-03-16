package com.eventproject.repository;

import com.eventproject.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepo extends JpaRepository<Blog, Long> {
    List<Blog> findAllByTitle(String title);
    Optional<Blog> findByTitle(String title);
    Blog findByBlogId(long idblog);
}
