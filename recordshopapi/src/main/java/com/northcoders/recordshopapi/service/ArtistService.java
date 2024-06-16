package com.northcoders.recordshopapi.service;

import com.northcoders.recordshopapi.models.Album;
import java.util.List;

public interface ArtistService {
    List<Album> getAllAlbumsByArtist(String artistName);
}

