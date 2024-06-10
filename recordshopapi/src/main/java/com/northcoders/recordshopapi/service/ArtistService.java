package com.northcoders.recordshopapi.service;

import com.northcoders.recordshopapi.models.Album;

import java.util.List;

public interface ArtistService {

    public List<Album> getAllAlbumsByArtist(String artistName);
}
