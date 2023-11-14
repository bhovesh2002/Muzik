package com.MusicPlayer.MuzikPlear.Controller;

import com.MusicPlayer.MuzikPlear.Model.MusicFile;
import com.MusicPlayer.MuzikPlear.Repository.MusicRepository;
import com.MusicPlayer.MuzikPlear.Service.MediaStreamLoader;
import com.sun.javafx.application.PlatformImpl;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;


import javax.sound.sampled.*;
import java.io.*;

@Controller
public class MusicController {

    MusicRepository musicRepository;

    MediaStreamLoader mediaLoaderService;
    private static final int BUFFER_SIZE = 4096;


    public MusicController(MusicRepository musicRepository, MediaStreamLoader mediaLoaderService){
        this.musicRepository = musicRepository;
        this.mediaLoaderService = mediaLoaderService;
    }


    @PostMapping("/save")
    public String saveSong(@RequestBody MultipartFile file) throws IOException {
        File createFile = new File(file.getName()+".m4a");
        OutputStream out = new FileOutputStream(createFile);
        out.write(file.getBytes());
        out.close();

        MusicFile musicFile = new MusicFile();
        musicFile.setName(file.getName());
        musicFile.setAddress("F:\\Spring Boot Projects\\New folder\\"+file.getName()+".m4a");

        musicRepository.save(musicFile);
        return "File Saved";
    }

    @GetMapping("/play")
    public String playSong(Model model){
//        MusicFile musicFile = musicRepository.findById(1).get();
        model.addAttribute("musicFiles", musicRepository.findAll());
        model.addAttribute("musicFile", new MusicFile());
        return "play";
    }

    //To stream file stored in local storage
    @GetMapping(value = "/start")
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> playMedia(/*@PathVariable("vid_id") String video_id,*/ @RequestHeader(value = "Range", required = false ) String rangeHeader){
        try {
            String filePath = "F:\\Spring Boot Projects\\New folder\\file.m4a";
            ResponseEntity<StreamingResponseBody> retVal = mediaLoaderService.loadPartialMediaFile(filePath,rangeHeader);
            return retVal;
        }catch (FileNotFoundException fnfe){
//            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (IOException ioe){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    @PostMapping("/save")
//    public String saveSong(@RequestBody File file){
//        try{
//            MusicFile musicFile = new MusicFile();
//            musicFile.setName("fileName");
//
//
////            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
////            musicRepository.save(musicFile);
////
//            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
//
//
//            musicFile.setFileData(stream.readAllBytes());
//            musicRepository.save(musicFile);
////            byte[] fileBytes =file.getBytes();
////
////            AudioFileFormat format = AudioSystem.getAudioFileFormat(file.getInputStream());
////
//            return "File saved";
//        }catch (IOException | UnsupportedAudioFileException ioe){
//            System.out.println("NO FILE FOUND.");
//            return "ERROR: NO FILE FOUND.";
////        } catch (UnsupportedAudioFileException e) {
////            throw new RuntimeException(e);
//        }
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
