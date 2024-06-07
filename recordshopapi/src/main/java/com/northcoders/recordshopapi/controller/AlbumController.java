package com.northcoders.recordshopapi.controller;

import com.northcoders.recordshopapi.models.Album;
import com.northcoders.recordshopapi.service.AlbumService;
import com.northcoders.recordshopapi.service.AlbumServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    public ResponseEntity<List<Album>> getAllAlbums() {
        return new ResponseEntity<>(albumService.getAllAlbums(), HttpStatus.OK);
    }

    @GetMapping("/:{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable Long id) {
        Album album = albumService.getAlbumById(id);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Album> addAlbum(@RequestBody Album album){
        return new ResponseEntity<>(albumService.addAlbum(album), HttpStatus.CREATED);
    }

    @PutMapping("/:{id}")
    public ResponseEntity<Album> updateAlbumById(
            @PathVariable long id,
            @RequestBody Album album
    ){
        return new ResponseEntity<>(albumService.updateBookById(id, album), HttpStatus.OK);
    }
}
