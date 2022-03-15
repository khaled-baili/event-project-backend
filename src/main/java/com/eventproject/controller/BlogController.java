package com.eventproject.controller;

import com.eventproject.model.*;
import com.eventproject.service.BlogService;
import com.eventproject.utility.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/blog")
public class BlogController {

    @Autowired private BlogService blogService;

    @GetMapping("/get-all-blogs")
    public ResponseEntity<List<Blog>>getBlogs() {
        return ResponseEntity.ok().body(blogService.getAllBlog());
    }

    @GetMapping("/get-blogs-title")
    public ResponseEntity<List<Blog>>getBlogByTitle(@RequestParam String title) {
        return ResponseEntity.ok().body(blogService.getBlogsByTitle(title));
    }

    @PostMapping("/save-blog")
    ResponseEntity<?> saveBlog(@Valid @RequestBody Blog blog) {
        if (blog == null) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.BAD_REQUEST,"You should provide information for saving blog"),
                    HttpStatus.BAD_REQUEST);
        } else if (blogService.titleExist(blog.getTitle()).isPresent()) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.CONFLICT,"This title already exist"),
                    HttpStatus.CONFLICT);
        }
        else {
            blogService.saveBlog(blog);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED,"blog Saved"),
                    HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/delete-blog")
    ResponseEntity<?> deleteBlog(@RequestParam Long idblog) {
        if (idblog == null)
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST,
                    "Provide id for deleting blog"), HttpStatus.BAD_REQUEST);
        else {
            blogService.deleteBlogById(idblog);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"Blog deleted successfully"),
                    HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete-all-blogs")
    ResponseEntity<?> deleteAllBlogs() {
        blogService.deleteAllBlogs();
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"Blogs deleted successfully"),
                HttpStatus.OK);
    }


}
