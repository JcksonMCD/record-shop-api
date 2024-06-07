package com.northcoders.recordshopapi.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "records")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "album_name")
    private String albumName;

    @Column(name = "artist")
    private String artist;

    @Column(name = "genre")
    private Genre genre;

    @Column(name = "release_year")
    private int releaseYear;

}
