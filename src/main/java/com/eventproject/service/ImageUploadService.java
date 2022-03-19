package com.eventproject.service;

import com.eventproject.model.ImageModel;
import com.eventproject.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImageUploadService {
    @Autowired
    private ImageRepository imageRepository;
    public ImageModel saveImage(ImageModel imageModel) {
        return imageRepository.save(imageModel);
    }

    public Optional<ImageModel> getImageByName(String name) {
        return imageRepository.findByName(name);
    }

    private void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }

    private void updateImage(ImageModel imageModel) {
        imageRepository.save(imageModel);
    }

}
