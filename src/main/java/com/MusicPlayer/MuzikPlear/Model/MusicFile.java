package com.MusicPlayer.MuzikPlear.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "songs")
public class MusicFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String artist;
    private String address;

    public MusicFile(int id, String name, String address, String artist) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.artist=artist;
    }

    public MusicFile(){}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
