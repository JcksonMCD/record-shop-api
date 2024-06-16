package com.northcoders.recordshopapi.service;

import com.northcoders.recordshopapi.exception.ItemNotFoundException;
import com.northcoders.recordshopapi.models.Album;
import com.northcoders.recordshopapi.models.Artist;
import com.northcoders.recordshopapi.models.Genre;
import com.northcoders.recordshopapi.repository.AlbumRepository;
import com.northcoders.recordshopapi.repository.ArtistRepository;
import com.northcoders.recordshopapi.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AlbumServiceImpl implements AlbumService {

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Override
    @Cacheable(value = "albums", key = "'allAlbums'")
    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }

    @Override
    @Cacheable(value = "albums", key = "#id")
    public Album getAlbumById(long id) {
        Optional<Album> album = albumRepository.findById(id);
        return album.orElseThrow(() -> new ItemNotFoundException(String.format("Album with id '%s' cannot be found", id)));
    }

    @Override
    @CacheEvict(value = "albums", key = "'allAlbums'")
    public Album addAlbum(Album album) {
        Artist albumArtist = artistRepository.findByName(album.getArtist().getName());
        if (albumArtist == null) {
            artistRepository.save(album.getArtist());
        } else {
            album.setArtist(albumArtist);
        }
        albumRepository.save(album);
        return album;
    }

    @Override
    @CacheEvict(value = "albums", key = "#id")
    public Album updateAlbumById(long id, Album album) {
        Optional<Album> optionalAlbum = albumRepository.findById(id);
        if (optionalAlbum.isPresent()) {
            Album existingAlbum = optionalAlbum.get();
            existingAlbum.setAlbumName(album.getAlbumName());
            existingAlbum.setGenre(album.getGenre());
            existingAlbum.setStockQuantity(album.getStockQuantity());
            existingAlbum.setReleaseYear(album.getReleaseYear());
            existingAlbum.setArtist(album.getArtist());
            albumRepository.save(existingAlbum);
            return existingAlbum;
        } else {
            throw new ItemNotFoundException(String.format("Album with id '%s' not found", id));
        }
    }

    @Override
    @CacheEvict(value = "albums", key = "'allAlbums'")
    public ResponseEntity<String> deleteById(long id) {
        if (albumRepository.existsById(id)) {
            albumRepository.deleteById(id);
            return new ResponseEntity<>("Album deleted successfully", HttpStatus.OK);
        } else {
            throw new ItemNotFoundException(String.format("Album with id '%s' not found", id));
        }
    }

    @Override
    @Cacheable(value = "albums", key = "#releaseYear")
    public List<Album> findAllByReleaseYear(int releaseYear) {
        List<Album> albums = albumRepository.findAllByReleaseYear(releaseYear);
        if (albums.isEmpty()) {
            throw new ItemNotFoundException(String.format("No albums found for release year '%s'", releaseYear));
        }
        return albums;
    }

    @Override
    @Cacheable(value = "albums", key = "#genre")
    public List<Album> findAllByGenre(Genre genre) {
        List<Album> albums = albumRepository.findByGenre(genre);
        if (albums.isEmpty()) {
            throw new ItemNotFoundException(String.format("No albums found for genre '%s'", genre));
        }
        return albums;
    }

    @Override
    @Cacheable(value = "albums", key = "#albumName")
    public Album findByAlbumName(String albumName) {
        Album album = albumRepository.findByAlbumName(albumName);
        if (album == null) {
            throw new ItemNotFoundException(String.format("Album with name '%s' not found", albumName));
        }
        return album;
    }
}

