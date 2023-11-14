package com.MusicPlayer.MuzikPlear.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
@Service
public interface MediaStreamLoader {

    ResponseEntity<StreamingResponseBody> loadEntireMediaFile(String localMediaFilePath) throws IOException;

    ResponseEntity<StreamingResponseBody> loadPartialMediaFile(String localMediaFilePath, String rangeValues) throws IOException;

    ResponseEntity<StreamingResponseBody> loadPartialMediaFile(String localMediaFilePath, long fileStartPOS, long fileEndPOS) throws IOException;

}
