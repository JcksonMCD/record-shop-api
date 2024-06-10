package com.northcoders.recordshopapi.repository;

import com.northcoders.recordshopapi.models.Album;
import com.northcoders.recordshopapi.models.Artist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends CrudRepository<Album, Long> {
    Album findByAlbumName(String name);
    List<Album> findByArtist(Artist artist);

    List<Album> findAllByReleaseYear(int releaseYear);
}
