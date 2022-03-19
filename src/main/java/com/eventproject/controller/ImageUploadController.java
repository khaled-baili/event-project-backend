package com.eventproject.controller;

import com.eventproject.model.ImageModel;
import com.eventproject.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/image")
public class ImageUploadController {
    @Autowired private ImageRepository imageRepository;

//    @PostMapping("/upload")
//    public ResponseEntity.BodyBuilder uploadImage(@RequestParam("imageFile") MultipartFile file ) throws IOException {
//        ImageModel imageModel = new ImageModel(file.getOriginalFilename(), file.getContentType(), )
//    }
}
