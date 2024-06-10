package com.northcoders.recordshopapi.service;

import com.northcoders.recordshopapi.models.Album;
import com.northcoders.recordshopapi.models.Artist;
import com.northcoders.recordshopapi.models.Genre;
import com.northcoders.recordshopapi.repository.AlbumRepository;
import com.northcoders.recordshopapi.repository.ArtistRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ArtistServiceImplTest {

    @Mock
    ArtistRepository artistRepository;

    @Mock
    AlbumRepository albumRepository;

    @InjectMocks
    ArtistServiceImpl artistServiceImpl;

    @Test
    void testGetAllAlbumsByArtist() {
        // Arrange
        String artistName = "Test Artist";
        Artist artist = new Artist();
        artist.setName(artistName);

        Album album1 = new Album(1L, "Album 1", artist, Genre.POP, 1990, 1);
        Album album2 = new Album(2L, "Album 2", artist, Genre.ROCK, 1999, 12);
        List<Album> albums = Arrays.asList(album1, album2);

        when(artistRepository.findByName(artistName)).thenReturn(artist);
        when(albumRepository.findByArtist(artist)).thenReturn(albums);

        // Act
        List<Album> albumsByArtist = artistServiceImpl.getAllAlbumsByArtist(artistName);

        // Assert
        assertNotNull(albumsByArtist);
        assertEquals(2, albumsByArtist.size());
        assertEquals("Album 1", albumsByArtist.get(0).getAlbumName());
        assertEquals("Album 2", albumsByArtist.get(1).getAlbumName());
        assertEquals(artist, albumsByArtist.get(0).getArtist());
        assertEquals(artist, albumsByArtist.get(1).getArtist());
    }
}