package com.northcoders.recordshopapi.controller;

import com.northcoders.recordshopapi.exception.ItemNotFoundException;
import com.northcoders.recordshopapi.models.Album;
import com.northcoders.recordshopapi.models.Artist;
import com.northcoders.recordshopapi.models.Genre;
import com.northcoders.recordshopapi.service.AlbumService;
import com.northcoders.recordshopapi.service.AlbumServiceImpl;
import com.northcoders.recordshopapi.service.ArtistService;
import com.northcoders.recordshopapi.service.ArtistServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/album")
public class AlbumController {
    @Autowired
    private AlbumService albumService;

    @Autowired
    private ArtistService artistService;

    @GetMapping
    public ResponseEntity<List<Album>> getAllAlbums() {
        return new ResponseEntity<>(albumService.getAllAlbums(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable Long id) {
        Album album = albumService.getAlbumById(id);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Album> addAlbum(@RequestBody Album album){
        return new ResponseEntity<>(albumService.addAlbum(album), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Album> updateAlbumById(
            @PathVariable("id") long albumId,
            @RequestBody Album updatedAlbum
    ){
        try {
            Album updated = albumService.updateAlbumById(albumId, updatedAlbum);
            if(updated == null) {
                return ResponseEntity.notFound().build(); // Album not found
            }
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Internal server error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAlbumById(@PathVariable long id){
        return albumService.deleteById(id);
    }

    @GetMapping("/artist")
    public ResponseEntity<List<Album>> findAlbumsByArtist(@RequestParam String artistName) {
        List<Album> albums = artistService.getAllAlbumsByArtist(artistName);
        return new ResponseEntity<>(albums, HttpStatus.OK);
    }

    @GetMapping("/year/{releaseYear}")
    public ResponseEntity<List<Album>> getAlbumsByReleaseYear(@PathVariable int releaseYear) {
        List<Album> albums = albumService.findAllByReleaseYear(releaseYear);
        return ResponseEntity.ok(albums);
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Album>> getAlbumsByGenre(@PathVariable Genre genre) {
        List<Album> albums = albumService.findAllByGenre(genre);
        return ResponseEntity.ok(albums);
    }

    /*
    get album information by album name
     */
}
