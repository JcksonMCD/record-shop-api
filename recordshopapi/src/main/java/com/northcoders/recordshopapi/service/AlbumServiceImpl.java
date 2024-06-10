package com.northcoders.recordshopapi.service;

import com.northcoders.recordshopapi.exception.ItemNotFoundException;
import com.northcoders.recordshopapi.models.Album;
import com.northcoders.recordshopapi.models.Artist;
import com.northcoders.recordshopapi.repository.AlbumRepository;
import com.northcoders.recordshopapi.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Album> getAllAlbums() {
        List<Album> albums = new ArrayList<>();
        albumRepository.findAll().forEach(albums::add);
        return albums;
    }

    @Override
    public Album getAlbumById(long id) {
        return albumRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException(id + ": Does not exist in the database"));
    }

    @Override
    public Album addAlbum(Album album) {    Artist albumArtist = artistRepository.findByName(album.getArtist().getName());
        if (albumArtist == null) {
            artistRepository.save(album.getArtist());
            albumRepository.save(album);
        } else {
            album.setArtist(albumArtist);
            if (albumRepository.findByAlbumName(album.getAlbumName()) == null) {
                albumRepository.save(album);
            } else {
                return null;
            }
        }
        return album;
    }

    @Override
    public Album updateAlbumById(long albumId, Album updatedAlbum) {
        Optional<Album> optionalAlbum = albumRepository.findById(albumId);
        if (optionalAlbum.isPresent()) {
            Album existingAlbum = optionalAlbum.get();

            // Update fields of existing album with values from updated album
            existingAlbum.setAlbumName(updatedAlbum.getAlbumName());
            existingAlbum.setArtist(updatedAlbum.getArtist());
            existingAlbum.setGenre(updatedAlbum.getGenre());
            existingAlbum.setReleaseYear(updatedAlbum.getReleaseYear());
            existingAlbum.setStockQuantity(updatedAlbum.getStockQuantity());

            // Save the updated album
            return albumRepository.save(existingAlbum);
        } else {
            // Album with given ID not found
            throw new ItemNotFoundException("Album not found with id: " + albumId);
        }
    }
    @Override
    public ResponseEntity<String> deleteById(long id) {
        albumRepository.deleteById(id);

        return new ResponseEntity<>("Album deleted at id " + id, HttpStatus.ACCEPTED);
    }


}
