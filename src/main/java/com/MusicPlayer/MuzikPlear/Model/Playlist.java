package com.MusicPlayer.MuzikPlear.Model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "playlists")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    private String name;
//    private Map<Integer, MusicFile> musicPlaylist;
    private List<Integer> musicPlaylist;

    public Playlist(int id, String name, /*Map<Integer, MusicFile> musicPlaylist*/ ArrayList<Integer> musicPlaylist) {
        Id = id;
        this.name = name;
        this.musicPlaylist = musicPlaylist;
    }

    public Playlist(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getMusicPlayList() {
        return musicPlaylist;
    }

    public void setMusicPlayList(ArrayList<Integer> musicPlayList) {
        this.musicPlaylist = musicPlayList;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
