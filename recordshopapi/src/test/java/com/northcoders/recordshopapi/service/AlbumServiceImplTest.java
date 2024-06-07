package com.northcoders.recordshopapi.service;

import com.northcoders.recordshopapi.models.Album;
import com.northcoders.recordshopapi.models.Artist;
import com.northcoders.recordshopapi.models.Genre;
import com.northcoders.recordshopapi.repository.AlbumRepository;
import lombok.Data;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
class AlbumServiceImplTest {

    @Mock
    AlbumRepository albumRepository;

    @InjectMocks
    AlbumServiceImpl albumServiceImpl;

    @Test
    void getAllAlbums() {
        // Arrange
        List<Album> albums = new ArrayList<>();
        albums.add(new Album(1L, "name", new Artist(), Genre.POP, 1990, 1));
        albums.add(new Album(2L, "name2", new Artist(), Genre.ROCK, 1999, 12));

        when(albumRepository.findAll()).thenReturn(albums);

        List<Album> actualResult = this.albumServiceImpl.getAllAlbums();

        Assertions.assertThat(actualResult).hasSize(2);
        Assertions.assertThat(actualResult).isEqualTo(albums);
    }

    @Test
    void getAlbumByIdTest(){
        Album album = new Album(2L, "name2", new Artist(), Genre.ROCK, 1999, 12);

        when(albumRepository.findById(2L)).thenReturn(Optional.of(album));

        albumServiceImpl.getAlbumById(2L);

        verify(albumRepository, times(1)).findById(2L);
        assertThat(albumServiceImpl.getAlbumById(2L)).isEqualTo(album);
    }

    @Test
    void addAlbumTest(){
        Album album = new Album(2L, "name2", new Artist(), Genre.ROCK, 1999, 12);

        when(albumRepository.save(album)).thenReturn(album);

        Album actualResult = albumServiceImpl.addAlbum(album);

        assertThat(actualResult).isEqualTo(album);
        verify(albumRepository, times(1)).save(album);
    }

    @Test
    void updateAlbumByIdTest(){
        Album album = new Album(2L, "name2", new Artist(), Genre.ROCK, 1999, 12);

        when(albumRepository.save(album)).thenReturn(album);

        Album actualResult = albumServiceImpl.updateBookById(2L, album);

        assertThat(actualResult).isEqualTo(album);
        verify(albumRepository, times(1)).save(album);
    }
}