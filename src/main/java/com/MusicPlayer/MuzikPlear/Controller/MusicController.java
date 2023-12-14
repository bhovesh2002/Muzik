package com.MusicPlayer.MuzikPlear.Controller;

import com.MusicPlayer.MuzikPlear.Model.MusicFile;
import com.MusicPlayer.MuzikPlear.Model.Playlist;
import com.MusicPlayer.MuzikPlear.Model.User;
import com.MusicPlayer.MuzikPlear.Repository.MusicRepository;
import com.MusicPlayer.MuzikPlear.Repository.PlaylistRepository;
import com.MusicPlayer.MuzikPlear.Repository.UserRepository;
import com.MusicPlayer.MuzikPlear.Service.MediaStreamLoader;
import com.sun.javafx.application.PlatformImpl;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.util.*;

@Controller
public class MusicController {

    MusicRepository musicRepository;
    PlaylistRepository playlistRepository;
    UserRepository userRepository;

    MediaStreamLoader mediaLoaderService;
    private static final int BUFFER_SIZE = 4096;


    public MusicController(MusicRepository musicRepository,UserRepository userRepository, PlaylistRepository playlistRepository, MediaStreamLoader mediaLoaderService){
        this.musicRepository = musicRepository;
        this.playlistRepository=playlistRepository;
        this.mediaLoaderService = mediaLoaderService;
        this.userRepository=userRepository;
    }

    @GetMapping("/")
    public String displayIndexPage(){
        return "index";
    }

    @GetMapping("/register")
    public String viewRegisterForm(Model model){
        model.addAttribute("user", new User());
        return "register_user";
    }

    @PostMapping("/register_user")
    public String registerUser(User user){
        User checkUserEmail=userRepository.findByEmail(user.getEmail());
        User checkUserUsername = userRepository.findByUsername(user.getUsername());
        if (checkUserUsername !=null || checkUserEmail != null){
            return "redirect:/register";
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return "redirect:/login";
    }



    @PostMapping("/save")
//    @ResponseBody
    public String saveSong(@RequestBody MultipartFile file, String songName, String artistName) throws IOException {
//        File createFile = new File(file.getName()+".m4a");
//        File createFile = new File(songName+".m4a");
        File createFile = new File("F:\\Spring Boot Projects\\New folder\\"+songName+".m4a");
        OutputStream out = new FileOutputStream(createFile);
        out.write(file.getBytes());
        out.close();

        MusicFile musicFile = new MusicFile();
        musicFile.setName(songName);
        musicFile.setArtist(artistName);
        musicFile.setAddress("F:\\Spring Boot Projects\\New folder\\"+songName+".m4a");

        musicRepository.save(musicFile);
        return "redirect:/play";
    }

    @GetMapping("/upload")
    public String uploadSongPage(Model model){
        return "upload_song";
    }

    @GetMapping("/play")
    public String playSong(Model model){
//        MusicFile musicFile = musicRepository.findById(1).get();
        model.addAttribute("musicFiles", musicRepository.findAll());
        model.addAttribute("musicFile", new MusicFile());
        return "play";
    }

    //To stream file stored in local storage
    @GetMapping(value = "/start/{song_id}")
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> playMedia(@PathVariable("song_id") String song_id, @RequestHeader(value = "Range", required = false ) String rangeHeader){
        try {
//            String filePath = "F:\\Spring Boot Projects\\New folder\\file.m4a";
            System.out.println("Song id:"+ song_id);
            int songId = Integer.valueOf(song_id);
            String filePath = musicRepository.findById(songId).get().getAddress();

            ResponseEntity<StreamingResponseBody> retVal = mediaLoaderService.loadPartialMediaFile(filePath,rangeHeader);
            return retVal;
        }catch (FileNotFoundException fnfe){
//            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (IOException ioe){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    @GetMapping(value = "/createpl")
//    public String searchMusic(Model model){
//        model.addAttribute("musicFiles", musicRepository.findAll());
//        model.addAttribute("musicFile", new MusicFile());
//        return "show_all";
//    }
    @PostMapping("/createpl")
    public String createPlaylist(Playlist playlist){
        if(playlistRepository.existsByName(playlist.getName())){
            return "redirect:/playlists";
        }
        playlistRepository.save(playlist);
        return "redirect:/edit/"+playlist.getName();
    }

    @GetMapping(value = "/playlists")
    public String displayPlaylists(Model model){
        model.addAttribute("playlists", playlistRepository.findAll());
        model.addAttribute("playlist", new Playlist());
        return "create_pl";
    }

    @GetMapping(value = "/edit/{name}")
    public String editPlaylist(@PathVariable("name")String name, Model model){
        if (!playlistRepository.existsByName(name)){
            //redirect to /createpl
            return "redirect:/playlists";
        }
        model.addAttribute("musicFiles", musicRepository.findAll());
        model.addAttribute("musicFile", new MusicFile());
        model.addAttribute("playlist", playlistRepository.findByName(name));
        return "add_to_pl";
    }

    @PostMapping(value = "/addtopl/{playlistName}/{mFID}")
    public String addToPl(@PathVariable("playlistName")String plName, @PathVariable("mFID") int id){
//        Playlist playlist = playlistRepository.findByName(plName);
//        if (playlist.getMusicPlayList().isEmpty() || playlist.getMusicPlayList() == null){
//            LinkedList<Integer> newList = new LinkedList<>();
//            newList.add(id);
//            playlist.setMusicPlayList(newList);
//            return "redirect:/edit/"+plName;
//        }
//        List<Integer> newList = new LinkedList<>();
//        System.out.println(playlistRepository.findByName(plName).getMusicPlayList() == null);
////        System.out.println(Arrays.toString(playlistRepository.findByName(plName).getMusicPlayList().toArray()));
//        if (playlistRepository.findByName(plName).getMusicPlayList() != null){
//            newList.addAll(playlistRepository.findByName(plName).getMusicPlayList());
//        }
//        newList.add(id);
//        playlistRepository.findByName(plName).setMusicPlayList((LinkedList<Integer>) newList);
////        System.out.println(Arrays.toString(playlistRepository.findByName(plName).getMusicPlayList().toArray()));
////        playlistRepository.findByName(plName).getMusicPlayList().forEach(System.out::println);
        Playlist newPlaylist = new Playlist();
        newPlaylist.setName(plName);
        newPlaylist.setId(playlistRepository.findByName(plName).getId());
        newPlaylist.setMusicPlayList((ArrayList<Integer>) playlistRepository.findByName(plName).getMusicPlayList());
        System.out.println(newPlaylist.getMusicPlayList() == null);
        if (newPlaylist.getMusicPlayList() == null){
            ArrayList<Integer> newList = new ArrayList<>();
            newPlaylist.setMusicPlayList(newList);
        }
        newPlaylist.getMusicPlayList().add(id);
        System.out.println(newPlaylist.getMusicPlayList());
        playlistRepository.deleteById(playlistRepository.findByName(plName).getId());
        playlistRepository.save(newPlaylist);
        System.out.println(newPlaylist.getMusicPlayList());
        return "redirect:/edit/"+plName;
    }

    @GetMapping("/play/playlist/{name}")
//    @ResponseBody
    public String playPlaylist(@PathVariable String name,Model model){
        List<MusicFile> musicFileList = new ArrayList<>();
        Playlist playlist = playlistRepository.findByName(name);
        List<Integer> musicPlaylist = playlist.getMusicPlayList();
        for (int i = 0; i<musicPlaylist.size();i++){
            Optional<MusicFile> optionalMusicFile = musicRepository.findById(musicPlaylist.get(i));
            if(optionalMusicFile.isPresent()){
                musicFileList.add(optionalMusicFile.get());
            }
//            MusicFile musicFile = musicRepository.findById(musicPlaylist.get(i)).get();
//            musicFileList.add(musicFile);
            System.out.println(i);
        }
        model.addAttribute("musicFiles",musicFileList);
        model.addAttribute("musicFile",new MusicFile());
        model.addAttribute("playlist",playlist);

        return "play_playlist";
    }

//    @PostMapping("/music/addtoplaylist/{id}")
//    public String removeFromPlaylist(@PathVariable int id){
//    }

//    @GetMapping("/get")
//    public String getSong(){
//        MusicFile musicFile = musicRepository.findById(1).get();
//
////        String audioFilePath = musicFile.getAddress();
//        String audioFilePath = "/audio/file.m4a";
//
//        PlatformImpl.startup(() -> {});
//
//        Media media = new Media(getClass().getResource(audioFilePath).toExternalForm());
//        MediaPlayer mediaPlayer = new MediaPlayer(media);
//        mediaPlayer.play();
//
//        Button playButton = new Button(">");
//        playButton.setOnAction(e ->{
//            if(playButton.getText().equals(">")){
//                mediaPlayer.play();
//                playButton.setText("||");
//            }else {
//                mediaPlayer.pause();
//                playButton.setText(">");
//            }
//        });
//
//
//        return "play";
//    }



//    @GetMapping("/get")
//    public String getSong() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
//        MusicFile musicFile = musicRepository.findById(1).get();
//
//        File file = new File(musicFile.getAddress());
//        System.out.println(musicFile.getAddress());
//
////        InputStream inputStream = new FileInputStream(file);
//        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(musicFile.getAddress());
//        AudioInputStream audioStream = AudioSystem.getAudioInputStream(inputStream);
//        AudioFormat audioFormat = audioStream.getFormat();
//        DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
//
//        SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
//        sourceDataLine.open(audioFormat);
//        sourceDataLine.start();
//
//        byte[] bufferBytes = new byte[BUFFER_SIZE];
//        int readBytes = -1;
//        while ((readBytes = audioStream.read(bufferBytes)) != -1){
//            sourceDataLine.write(bufferBytes, 0, readBytes);
//        }
//
//        sourceDataLine.drain();
//        sourceDataLine.close();
//        audioStream.close();
//
//        return "Playing song";
//    }

//    @GetMapping("/get")
//    public void getSong() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
//        MusicFile musicFile = musicRepository.findById(1).get();
//        byte[] audioData = musicFile.getFileData();
////         = musicFile.getFileFormat();
//        InputStream stream = new ByteArrayInputStream(audioData);
//        AudioInputStream audioStream = (AudioInputStream) stream;
//        AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(audioStream);
//
//
//        SourceDataLine line;
//        DataLine.Info info = new DataLine.Info(SourceDataLine.class, fileFormat.getFormat());
//
//        line = (SourceDataLine) AudioSystem.getLine(info);
//        line.open(fileFormat.getFormat());
//        line.start();
//        line.write(audioData, 0, audioData.length);
//
//
//
////        line = (SourceDataLine) AudioSystem.getLine(info);
////
////        final int bufferSize = 2200;
////        line.open(fileFormat.getFormat(), bufferSize);
////        line.start();
////
////        byte counter = 0;
////        final byte[] buffer = new byte[bufferSize];
////        byte sign = 1;
////        while (frame)
//
//
//
////        AudioFormat format = new
////        AudioSystem.getSourceDataLine();
////        AudioInputStream stream = new AudioInputStream(musicFile.getFileData());
////        return musicFile.getFileData();
//    }

}
