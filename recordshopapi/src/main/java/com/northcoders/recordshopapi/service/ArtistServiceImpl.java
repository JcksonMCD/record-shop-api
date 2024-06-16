package com.northcoders.recordshopapi.service;

import com.northcoders.recordshopapi.exception.ItemNotFoundException;
import com.northcoders.recordshopapi.models.Album;
import com.northcoders.recordshopapi.models.Artist;
import com.northcoders.recordshopapi.repository.AlbumRepository;
import com.northcoders.recordshopapi.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistServiceImpl implements ArtistService {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    AlbumRepository albumRepository;

    public List<Album> getAllAlbumsByArtist(String artistName) {
        Artist artist = artistRepository.findByName(artistName);
        if (artist == null) {
            throw new ItemNotFoundException(String.format("No albums can be found with artist name: '%s'", artistName));
        } else {
            return albumRepository.findByArtist(artist);
        }
    }
}


