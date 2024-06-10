package com.northcoders.recordshopapi.repository;

import com.northcoders.recordshopapi.models.Artist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends CrudRepository<Artist, Long> {

    Artist findByName(String name);
}
