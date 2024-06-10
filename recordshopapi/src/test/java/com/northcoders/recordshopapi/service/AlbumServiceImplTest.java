package com.northcoders.recordshopapi.service;

import com.northcoders.recordshopapi.models.Album;
import com.northcoders.recordshopapi.models.Artist;
import com.northcoders.recordshopapi.models.Genre;
import com.northcoders.recordshopapi.repository.AlbumRepository;
import com.northcoders.recordshopapi.repository.ArtistRepository;
import lombok.Data;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AlbumServiceImplTest {

    @Mock
    AlbumRepository albumRepository;

    @Mock
    ArtistRepository artistRepository;

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
    void testAddAlbum_NewArtist() {
        // Arrange
        Album album = new Album();
        album.setAlbumName("Test Album");
        Artist artist = new Artist();
        artist.setName("Test Artist");
        album.setArtist(artist);

        when(artistRepository.findByName("Test Artist")).thenReturn(null);
        when(albumRepository.findByAlbumName("Test Album")).thenReturn(null);

        // Act
        Album addedAlbum = albumServiceImpl.addAlbum(album);

        // Assert
        assertNotNull(addedAlbum);
        assertEquals("Test Album", addedAlbum.getAlbumName());
        assertEquals("Test Artist", addedAlbum.getArtist().getName());
        verify(artistRepository, times(1)).save(artist);
        verify(albumRepository, times(1)).save(album);
    }

    @Test
    void testAddAlbum_ExistingArtist() {
        // Arrange
        Album album = new Album();
        album.setAlbumName("Test Album");
        Artist artist = new Artist();
        artist.setName("Test Artist");
        album.setArtist(artist);

        when(artistRepository.findByName("Test Artist")).thenReturn(artist);
        when(albumRepository.findByAlbumName("Test Album")).thenReturn(null);

        // Act
        Album addedAlbum = albumServiceImpl.addAlbum(album);

        // Assert
        assertNotNull(addedAlbum);
        assertEquals("Test Album", addedAlbum.getAlbumName());
        assertEquals("Test Artist", addedAlbum.getArtist().getName());
        verify(artistRepository, never()).save(artist);
        verify(albumRepository, times(1)).save(album);
    }

    @Test
    void testAddAlbum_AlbumExists() {
        // Arrange
        Album album = new Album();
        album.setAlbumName("Test Album");
        Artist artist = new Artist();
        artist.setName("Test Artist");
        album.setArtist(artist);

        when(artistRepository.findByName("Test Artist")).thenReturn(artist);
        when(albumRepository.findByAlbumName("Test Album")).thenReturn(album);

        // Act
        Album addedAlbum = albumServiceImpl.addAlbum(album);

        // Assert
        assertNull(addedAlbum);
        verify(artistRepository, never()).save(artist);
        verify(albumRepository, never()).save(album);
    }

    @Test
    void updateAlbumByIdTest(){
        Album album = new Album(2L, "name2", new Artist(), Genre.ROCK, 1999, 12);

        when(albumRepository.save(album)).thenReturn(album);

        Album actualResult = albumServiceImpl.updateBookById(2L, album);

        assertThat(actualResult).isEqualTo(album);
        verify(albumRepository, times(1)).save(album);
    }

    @Test
    void deleteByIdTest(){
        ResponseEntity<String> result = albumServiceImpl.deleteById(1L);

        verify(albumRepository, times(1)).deleteById(1L);
        assertThat(result).isEqualTo(new ResponseEntity<String>("Album deleted at id 1", HttpStatus.ACCEPTED));
    }
}