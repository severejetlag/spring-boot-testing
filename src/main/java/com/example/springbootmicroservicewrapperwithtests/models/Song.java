package com.example.springbootmicroservicewrapperwithtests.models;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity @Table(name = "SONGS")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TITLE")
    private String userName;

    @Column(name = "ARTIST")
    private String firstName;

    public Song(String title, String artist) {
        this.userName = title;
        this.firstName = artist;
    }
}