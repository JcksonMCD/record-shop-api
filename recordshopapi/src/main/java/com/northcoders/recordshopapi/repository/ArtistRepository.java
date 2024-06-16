package com.northcoders.recordshopapi.repository;

import com.northcoders.recordshopapi.models.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Artist findByName(String name);
}

