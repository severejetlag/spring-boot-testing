package com.example.springbootmicroservicewrapperwithtests.features;

import com.example.springbootmicroservicewrapperwithtests.models.Song;
import com.example.springbootmicroservicewrapperwithtests.repositories.SongRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SongsApiFeatureTest {

    @Autowired
    private SongRepository SongRepository;

    Song secondSong;

    @Before
    public void setUp() {
        SongRepository.deleteAll();

        Song firstSong = new Song(
                "Ima",
                "Song"
        );

        secondSong = new Song(
                "Imma'nother",
                "Song"
        );

        Stream.of(firstSong, secondSong)
                .forEach(Song -> {
                    SongRepository.save(Song);
                });
    }

    @After
    public void tearDown() {
        SongRepository.deleteAll();
    }

    @Test
    public void shouldAllowFullCrudForASong() throws Exception {

        // Test creating a Song
        Song SongNotYetInDb = new Song(
                "new_song not",
                "Yet Created"
        );

        given()
                .contentType(JSON)
                .and().body(SongNotYetInDb)
                .when()
                .post("http://localhost:8080/songs")
                .then()
                .statusCode(is(200))
                .and().body(containsString("new_song"));

        // Test get all Songs
        when()
                .get("http://localhost:8080/songs/")
                .then()
                .statusCode(is(200))
                .and().body(containsString("Ima"))
                .and().body(containsString("Song"))
                .and().body(containsString("Yet Created"));

        // Test finding one Song by ID
        when()
                .get("http://localhost:8080/songs/" + secondSong.getId())
                .then()
                .statusCode(is(200))
                .and().body(containsString("Imma'nother"))
                .and().body(containsString("Song"));

        // Test updating a Song
        secondSong.setTitle("changed_title");

        given()
                .contentType(JSON)
                .and().body(secondSong)
                .when()
                .patch("http://localhost:8080/songs/" + secondSong.getId())
                .then()
                .statusCode(is(200))
                .and().body(containsString("changed_title"));

        // Test deleting a Song
        when()
                .delete("http://localhost:8080/songs/" + secondSong.getId())
                .then()
                .statusCode(is(200));
    }
}