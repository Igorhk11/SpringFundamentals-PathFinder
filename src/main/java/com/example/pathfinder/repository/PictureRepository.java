package com.example.pathfinder.repository;

import com.example.pathfinder.model.entitiy.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PictureRepository extends JpaRepository<Picture, Long> {

    @Query("SELECT p.url From Picture p")
    List<String> findAllUrls();
}
