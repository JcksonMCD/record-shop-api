package com.northcoders.recordshopapi.controller;

import com.northcoders.recordshopapi.models.Album;
import com.northcoders.recordshopapi.models.Genre;
import com.northcoders.recordshopapi.service.AlbumService;
import com.northcoders.recordshopapi.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return new ResponseEntity<>(albumService.getAlbumById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Album> addAlbum(@RequestBody Album album) {
        Album createdAlbum = albumService.addAlbum(album);
        if (createdAlbum != null) {
            return new ResponseEntity<>(createdAlbum, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Album> updateTodoItem(@PathVariable("id") Long id, @RequestBody Album album) {
        return new ResponseEntity<>(albumService.updateAlbumById(id, album), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAlbumById(@PathVariable long id) {
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

    @GetMapping("/name/{albumName}")
    public ResponseEntity<Album> getAlbumByName(@PathVariable String albumName) {
        Album album = albumService.findByAlbumName(albumName);
        return ResponseEntity.ok(album);
    }
}
