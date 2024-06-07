package com.northcoders.recordshopapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.northcoders.recordshopapi.models.Album;
import com.northcoders.recordshopapi.models.Artist;
import com.northcoders.recordshopapi.models.Genre;
import com.northcoders.recordshopapi.service.AlbumServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class AlbumControllerTest {
    @Mock
    private AlbumServiceImpl albumServiceImpl;

    @InjectMocks
    private AlbumController albumController;

    @Autowired
    private MockMvc mockMvcController;

    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        openMocks(this);

        mockMvcController = MockMvcBuilders.standaloneSetup(albumController).build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
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
        this.mockMvcController.perform(MockMvcRequestBuilders.get("/api/v1/album"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].albumName").value(albums.get(0).getAlbumName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].albumName").value(albums.get(1).getAlbumName()));

    }

    @Test
    @DisplayName("GET /album by Id")
    void getAllAlbumsByIdTest() throws Exception {
        // Arrange
        Album album = new Album(1L, "name", new Artist(), Genre.POP, 1990, 1);

        when(albumServiceImpl.getAlbumById(1)).thenReturn(album);

        // Act & Assert
        this.mockMvcController.perform(
                        MockMvcRequestBuilders.get("/api/v1/album/:1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.albumName").value(album.getAlbumName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stockQuantity").value(album.getStockQuantity()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(album.getGenre().toString()));
        verify(albumServiceImpl, times(1)).getAlbumById(1);
    }

    @Test
    @DisplayName("POST /album")
    void postAlbumTest() throws Exception {
        // Arrange
        Album album = new Album(1L, "name", new Artist(), Genre.POP, 1990, 1);

        when(albumServiceImpl.addAlbum(album)).thenReturn(album);
        // act
        this.mockMvcController.perform(
                        MockMvcRequestBuilders.post("/api/v1/album")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(album)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("PUT /album by Id")
    void updateAlbumByIdTest() throws Exception {
        // Arrange
        Album album = new Album(1L, "name", new Artist(), Genre.POP, 1990, 1);

        when(albumServiceImpl.updateBookById(1L, album)).thenReturn(album);

        // Act & Assert
        this.mockMvcController.perform(
                        MockMvcRequestBuilders.put("/api/v1/album/:1").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").content(mapper.writeValueAsString(album)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.albumName").value(album.getAlbumName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stockQuantity").value(album.getStockQuantity()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(album.getGenre().toString()));
        verify(albumServiceImpl, times(1)).updateBookById(1L, album);
    }

    @Test
    public void testDeleteById() throws Exception {

        when(albumServiceImpl.deleteById(1)).thenReturn(new ResponseEntity<String>("Test passed", HttpStatus.ACCEPTED));

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.delete("/api/v1/album/:1"))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().string("Test passed"));


        verify(albumServiceImpl, times(1)).deleteById(1);
    }
}