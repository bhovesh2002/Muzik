package com.MusicPlayer.MuzikPlear.Repository;

import com.MusicPlayer.MuzikPlear.Model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {

    public Boolean existsByName(String name);

    public Playlist findByName(String name);

}
