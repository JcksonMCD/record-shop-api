package com.northcoders.recordshopapi.repository;

import com.northcoders.recordshopapi.models.Album;
import com.northcoders.recordshopapi.models.Artist;
import com.northcoders.recordshopapi.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findAllByReleaseYear(int releaseYear);

    List<Album> findByGenre(Genre genre);

    List<Album> findByArtist(Artist artist);

    Album findByAlbumName(String albumName);
}
