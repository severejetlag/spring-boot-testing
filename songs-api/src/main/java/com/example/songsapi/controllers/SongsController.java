package com.example.songsapi.controllers;

import com.example.songsapi.models.Song;
import com.example.songsapi.repositories.SongRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@RestController
public class SongsController {
    @Autowired
    private SongRepository songRepository;

    @GetMapping("/")
    public Iterable<Song> findAllSongs() {
        return songRepository.findAll();
    }

    @GetMapping("/{songId}")
    public Song findSongById(@PathVariable Long songId) throws NotFoundException {

        Song foundSong = songRepository.findOne(songId);

        if (foundSong == null) {
            throw new NotFoundException("Song with ID of " + songId + " was not found!");
        }

        return foundSong;
    }

    @DeleteMapping("/{songId}")
    public HttpStatus deleteSongById(@PathVariable Long songId) throws EmptyResultDataAccessException {

        songRepository.delete(songId);
        return HttpStatus.OK;
    }

    // Exception handlers

    @ExceptionHandler
    void handleSongNotFound(
            NotFoundException exception,
            HttpServletResponse response) throws IOException {

        response.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    @ExceptionHandler
    void handleDeleteNotFoundException(
            EmptyResultDataAccessException exception,
            HttpServletResponse response) throws IOException {

        response.sendError(HttpStatus.NOT_FOUND.value());
    }
}
