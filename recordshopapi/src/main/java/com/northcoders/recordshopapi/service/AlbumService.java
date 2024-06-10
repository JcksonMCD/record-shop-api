package com.northcoders.recordshopapi.service;

import com.northcoders.recordshopapi.models.Album;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AlbumService {
    List<Album> getAllAlbums();

    Album addAlbum(Album album);

    Album getAlbumById(long id);

    Album updateAlbumById(long albumId, Album updatedAlbum);

    ResponseEntity<String> deleteById(long id);
}
