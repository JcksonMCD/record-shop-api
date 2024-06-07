package com.northcoders.recordshopapi.repository;

import com.northcoders.recordshopapi.models.Album;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends CrudRepository<Album, Long> {
}
