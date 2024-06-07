package com.northcoders.recordshopapi.service;

import com.northcoders.recordshopapi.exception.ItemNotFoundException;
import com.northcoders.recordshopapi.models.Album;
import com.northcoders.recordshopapi.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.northcoders.recordshopapi.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlbumServiceImpl implements AlbumService {

    @Autowired
    AlbumRepository albumRepository;

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
    public Album addAlbum(Album album) {
        albumRepository.save(album);
        return album;
    }

    @Override
    public Album updateBookById(long albumId, Album album) {
            album.setId(albumId);
//            album.setGenre(album.getGenre());
//            album.setAlbumName(album.getAlbumName());
//            album.setGenre(album.getGenre());
//            album.setArtist(album.getArtist());
//            album.setReleaseYear(album.getReleaseYear());
//            album.setStockQuantity(album.getStockQuantity());
            albumRepository.save(album);
            return album;
    }


}
