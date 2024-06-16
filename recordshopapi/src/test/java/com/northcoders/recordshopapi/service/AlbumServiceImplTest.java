package com.northcoders.recordshopapi.service;

import com.northcoders.recordshopapi.exception.ItemNotFoundException;
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
import java.util.Arrays;
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
    public void updateAlbumById_AlbumExists() {
        // Arrange
        Album existingAlbum = new Album(1L, "old name", new Artist(1L, "old artist", null), Genre.POP, 1980, 5);
        Album updatedAlbum = new Album(1L, "new name", new Artist(2L, "new artist", null), Genre.ROCK, 1990, 10);

        when(albumRepository.findById(1L)).thenReturn(Optional.of(existingAlbum));
        when(albumRepository.save(any(Album.class))).thenReturn(updatedAlbum);

        // Act
        Album result = albumServiceImpl.updateAlbumById(1L, updatedAlbum);

        // Assert
        assertEquals("new name", result.getAlbumName());
        assertEquals("new artist", result.getArtist().getName());
        assertEquals(Genre.ROCK, result.getGenre());
        assertEquals(1990, result.getReleaseYear());
        assertEquals(10, result.getStockQuantity());

        verify(albumRepository, times(1)).findById(1L);
    }

    @Test
    public void updateAlbumById_AlbumNotExists() {
        // Arrange
        Album updatedAlbum = new Album(1L, "new name", new Artist(2L, "new artist", null), Genre.ROCK, 1990, 10);

        when(albumRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ItemNotFoundException.class, () -> {
            albumServiceImpl.updateAlbumById(1L, updatedAlbum);
        });

        verify(albumRepository, times(1)).findById(1L);
        verify(albumRepository, never()).save(any(Album.class));
    }

    @Test
    void deleteByIdTest(){
        ResponseEntity<String> result = albumServiceImpl.deleteById(1L);

        verify(albumRepository, times(1)).deleteById(1L);
        assertThat(result).isEqualTo(new ResponseEntity<String>("Album deleted at id 1", HttpStatus.ACCEPTED));
    }

    @Test
    public void findAllByReleaseYear() {
        // Arrange
        Album album1 = new Album(1L, "Album1", null, Genre.POP, 2000, 10);
        Album album2 = new Album(2L, "Album2", null, Genre.ROCK, 2000, 15);
        List<Album> albums = Arrays.asList(album1, album2);

        when(albumRepository.findAllByReleaseYear(2000)).thenReturn(albums);

        // Act
        List<Album> result = albumServiceImpl.findAllByReleaseYear(2000);

        // Assert
        assertEquals(2, result.size());
        verify(albumRepository, times(1)).findAllByReleaseYear(2000);
    }

    @Test
    void findAllByGenreTest() {
        // Arrange
        Album album1 = new Album(1L, "Album1", null, Genre.POP, 2000, 10);
        Album album2 = new Album(2L, "Album2", null, Genre.POP, 2005, 15);
        List<Album> albums = Arrays.asList(album1, album2);

        when(albumRepository.findByGenre(Genre.POP)).thenReturn(albums);

        // Act
        List<Album> result = albumServiceImpl.findAllByGenre(Genre.POP);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Album1", result.get(0).getAlbumName());
        assertEquals("Album2", result.get(1).getAlbumName());
    }

    @Test
    void findByAlbumNameTest() {
        // Arrange
        Album album = new Album(1L, "Album1", null, Genre.POP, 2000, 10);

        when(albumRepository.findByAlbumName("Album1")).thenReturn(album);

        // Act
        Album result = albumServiceImpl.findByAlbumName("Album1");

        // Assert
        assertEquals("Album1", result.getAlbumName());
    }
}