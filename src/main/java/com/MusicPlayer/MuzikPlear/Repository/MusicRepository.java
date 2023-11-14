package com.MusicPlayer.MuzikPlear.Repository;

import com.MusicPlayer.MuzikPlear.Model.MusicFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<MusicFile, Integer> {

}
