package com.northcoders.recordshopapi.service;

import com.northcoders.recordshopapi.models.Album;
import com.northcoders.recordshopapi.models.Artist;
import com.northcoders.recordshopapi.repository.AlbumRepository;
import com.northcoders.recordshopapi.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

public class ArtistServiceImpl implements ArtistService{

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    AlbumRepository albumRepository;

    public List<Album> getAllAlbumsByArtist(String artistName) {
        Artist artist = artistRepository.findByName(artistName);
        if (artist == null) {
            return Collections.emptyList();
        } else {
            return albumRepository.findByArtist(artist);
        }
    }
}
