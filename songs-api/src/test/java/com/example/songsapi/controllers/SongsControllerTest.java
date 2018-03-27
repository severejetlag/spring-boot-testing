package com.example.songsapi.controllers;

import com.example.songsapi.models.Song;
import com.example.songsapi.repositories.SongRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SongsController.class)

public class SongsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SongRepository mockSongRepository;

    @Before
    public void setUp() {
        Song firstSong = new Song(
                "Ima",
                "Song"
        );

        Song secondSong = new Song(
                "Imma'nother",
                "Song"
        );

        Iterable<Song> mockSongs =
                Stream.of(firstSong, secondSong).collect(Collectors.toList());

        given(mockSongRepository.findAll()).willReturn(mockSongs);
        given(mockSongRepository.findOne(1L)).willReturn(firstSong);
        given(mockSongRepository.findOne(4L)).willReturn(null);

        // Mock out Delete to return EmptyResultDataAccessException for missing song with ID of 4
        doAnswer(invocation -> {
            throw new EmptyResultDataAccessException("ERROR MESSAGE FROM MOCK!!!", 1234);
        }).when(mockSongRepository).delete(4L);
    }

    @Test
    public void findAllSongs_success_returnsStatusOK() throws Exception {

        this.mockMvc
                .perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    public void findAllSongs_success_returnAllSongsAsJSON() throws Exception {

        this.mockMvc
                .perform(get("/"))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void findAllSongs_success_returnTitleForEachSong() throws Exception {

        this.mockMvc
                .perform(get("/"))
                .andExpect(jsonPath("$[0].title", is("Ima")));
    }

    @Test
    public void findAllSongs_success_returnArtistForEachSong() throws Exception {

        this.mockMvc
                .perform(get("/"))
                .andExpect(jsonPath("$[0].artist", is("Song")));
    }

    @Test
    public void findSongById_success_returnsStatusOK() throws Exception {

        this.mockMvc
                .perform(get("/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void findSongById_success_returnTitle() throws Exception {

        this.mockMvc
                .perform(get("/1"))
                .andExpect(jsonPath("$.title", is("Ima")));
    }

    @Test
    public void findSongById_success_returnArtist() throws Exception {

        this.mockMvc
                .perform(get("/1"))
                .andExpect(jsonPath("$.artist", is("Song")));
    }

    @Test
    public void findSongById_failure_songNotFoundReturns404() throws Exception {

        this.mockMvc
                .perform(get("/4"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findSongById_failure_songNotFoundReturnsNotFoundErrorMessage() throws Exception {

        this.mockMvc
                .perform(get("/4"))
                .andExpect(status().reason(containsString("Song with ID of 4 was not found!")));
    }

    @Test
    public void deleteSongById_success_returnsStatusOk() throws Exception {

        this.mockMvc
                .perform(delete("/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteSongById_success_deletesViaRepository() throws Exception {

        this.mockMvc.perform(delete("/1"));

        verify(mockSongRepository, times(1)).delete(1L);
    }

    @Test
    public void deleteSongById_failure_songNotFoundReturns404() throws Exception {

        this.mockMvc
                .perform(delete("/4"))
                .andExpect(status().isNotFound());
    }
}
