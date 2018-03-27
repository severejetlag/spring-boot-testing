package com.example.songsapi.repositories;

import com.example.songsapi.models.Song;
import com.google.common.collect.Iterables;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SongRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SongRepository songRepository;

    @Before
    public void setUp() {
        Song firstSong = new Song(
                "some title",
                "some artist"
        );

        Song secondSong = new Song(
                "some other title",
                "some other artist"
        );

        entityManager.persist(firstSong);
        entityManager.persist(secondSong);
        entityManager.flush();
    }

    @Test
    public void findAll_returnsAllSongs() {
        Iterable<Song> songsFromDb = songRepository.findAll();

        assertThat(Iterables.size(songsFromDb), is(3));
    }

}