package com.northcoders.recordshopapi.service;

import com.northcoders.recordshopapi.models.Album;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AlbumService {
    List<Album> getAllAlbums();

    Album addAlbum(Album album);

    Album getAlbumById(long id);
}