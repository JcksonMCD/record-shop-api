package com.northcoders.recordshopapi.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "artists")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "artist_name")
    private String name;

    @OneToMany
    @JoinColumn(name = "album_name")
    private List<Album> albums;

    public void setAlbumName(List<Album> albums) {
        this.albums = albums;
    }
}
