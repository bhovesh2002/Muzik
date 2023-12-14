package com.MusicPlayer.MuzikPlear.Service;

import com.MusicPlayer.MuzikPlear.Model.MusicFile;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Map;

@Service
public interface PlaylistService {

    LinkedList<Integer> getAllMusicFiles();

}