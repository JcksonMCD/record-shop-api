package com.northcoders.recordshopapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.northcoders.recordshopapi.models.Album;
import com.northcoders.recordshopapi.models.Artist;
import com.northcoders.recordshopapi.models.Genre;
import com.northcoders.recordshopapi.service.AlbumService;
import com.northcoders.recordshopapi.service.ArtistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AlbumControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlbumService albumServiceImpl;

    @MockBean
    private ArtistService artistServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private AlbumController albumController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(albumController).build();
    }

    @Test
    @DisplayName("GET /album")
    void getAllAlbumsTest() throws Exception {
        // Arrange
        List<Album> albums = new ArrayList<>();
        albums.add(new Album(1L, "name", new Artist(), Genre.POP, 1990, 1));
        albums.add(new Album(2L, "name2", new Artist(), Genre.ROCK, 1999, 12));

        when(albumServiceImpl.getAllAlbums()).thenReturn(albums);

        // Act & Assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/album"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].albumName").value(albums.get(0).getAlbumName()))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].albumName").value(albums.get(1).getAlbumName()));
    }

    @Test
    @DisplayName("GET /album by Id")
    void getAllAlbumsByIdTest() throws Exception {
        // Arrange
        Album album = new Album(1L, "name", new Artist(), Genre.POP, 1990, 1);

        when(albumServiceImpl.getAlbumById(1)).thenReturn(album);

        // Act & Assert
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/album/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.albumName").value(album.getAlbumName()))
                .andExpect(jsonPath("$.stockQuantity").value(album.getStockQuantity()))
                .andExpect(jsonPath("$.genre").value(album.getGenre().toString()));
        verify(albumServiceImpl, times(1)).getAlbumById(1);
    }

    @Test
    @DisplayName("POST /album")
    void postAlbumTest() throws Exception {
        // Arrange
        Album album = new Album(1L, "name", new Artist(), Genre.POP, 1990, 1);

        when(albumServiceImpl.addAlbum(any(Album.class))).thenReturn(album);

        // Act & Assert
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/album")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(album)))
                .andExpect(status().isCreated());

        verify(albumServiceImpl, times(1)).addAlbum(any(Album.class));
    }

    @Test
    public void updateAlbumByIdTest() throws Exception {
        Album album = new Album(1L, "name", new Artist(), Genre.POP, 1990, 1);
        when(albumServiceImpl.updateAlbumById(anyLong(), any(Album.class))).thenReturn(album);

        mockMvc.perform(put("/api/v1/album/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"albumName\":\"name\",\"artist\":{\"id\":0,\"name\":null},\"genre\":\"POP\",\"releaseYear\":1990,\"stockQuantity\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.albumName").value("name"));
    }

    @Test
    public void testDeleteById() throws Exception {

        when(albumServiceImpl.deleteById(1)).thenReturn(new ResponseEntity<String>("Test passed", HttpStatus.ACCEPTED));

        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/album/1"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Test passed"));

        verify(albumServiceImpl, times(1)).deleteById(1);
    }

    @Test
    void testFindAlbumsByArtist() throws Exception {
        // Arrange
        Artist artist = new Artist();
        artist.setName("Test Artist");
        List<Album> albums = new ArrayList<>();
        albums.add(new Album(1L, "Album 1", artist, Genre.POP, 2000, 10));
        albums.add(new Album(2L, "Album 2", artist, Genre.ROCK, 2005, 12));

        when(artistServiceImpl.getAllAlbumsByArtist("Test Artist")).thenReturn(albums);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/album/artist")
                        .param("artistName", "Test Artist")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].albumName").value("Album 1"))
                .andExpect(jsonPath("$[1].albumName").value("Album 2"));

        verify(artistServiceImpl, times(1)).getAllAlbumsByArtist("Test Artist");
    }

    @Test
    public void getAlbumsByReleaseYear() throws Exception {
        // Arrange
        Album album1 = new Album(1L, "Album1", null, Genre.POP, 2000, 10);
        Album album2 = new Album(2L, "Album2", null, Genre.ROCK, 2000, 15);
        List<Album> albums = Arrays.asList(album1, album2);

        when(albumServiceImpl.findAllByReleaseYear(2000)).thenReturn(albums);

        // Act & Assert
        mockMvc.perform(get("/api/v1/album/year/2000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].albumName").value("Album1"))
                .andExpect(jsonPath("$[0].genre").value(Genre.POP.toString()))
                .andExpect(jsonPath("$[0].releaseYear").value(2000))
                .andExpect(jsonPath("$[0].stockQuantity").value(10))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].albumName").value("Album2"))
                .andExpect(jsonPath("$[1].genre").value(Genre.ROCK.toString()))
                .andExpect(jsonPath("$[1].releaseYear").value(2000))
                .andExpect(jsonPath("$[1].stockQuantity").value(15));

        verify(albumServiceImpl, times(1)).findAllByReleaseYear(2000);
    }

    @Test
    @DisplayName("GET /album/genre/{genre}")
    void getAlbumsByGenreTest() throws Exception {
        // Arrange
        Album album1 = new Album(1L, "Album1", null, Genre.POP, 2000, 10);
        Album album2 = new Album(2L, "Album2", null, Genre.POP, 2005, 15);
        List<Album> albums = Arrays.asList(album1, album2);

        when(albumServiceImpl.findAllByGenre(Genre.POP)).thenReturn(albums);

        // Act & Assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/album/genre/POP")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].albumName").value("Album1"))
                .andExpect(jsonPath("$[1].albumName").value("Album2"));
    }
}
