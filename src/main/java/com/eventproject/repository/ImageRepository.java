package com.eventproject.repository;

import com.eventproject.model.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageModel, Long> {
    @Transactional
    Optional<ImageModel> findByName(String name);

    @Transactional
    void deleteById(Long id);

}
