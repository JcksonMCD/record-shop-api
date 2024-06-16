package com.northcoders.recordshopapi.models;


import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "album_name")
    private String albumName;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Column(name = "release_year")
    private int releaseYear;

    @Column(name = "stock_quantity")
    private int stockQuantity;
}
