package com.janterertube.videos.controller;

import com.janterertube.videos.exceptions.InvalidFileFormatException;
import com.janterertube.videos.model.FileUploadResponse;
import com.janterertube.videos.service.UploadService;
import com.janterertube.videos.util.CommonUtil;
import com.janterertube.videos.util.VideoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static com.janterertube.videos.constants.AppConstant.UPLOAD_DIR;
import static com.janterertube.videos.constants.AppConstant.UPLOAD_DIR_RELATIVIZE;

@RestController
@Slf4j
public class AppController {

    private final UploadService uploadService;

    private final VideoUtil videoUtil;

    public AppController(UploadService uploadService, VideoUtil videoUtil) {
        this.uploadService = uploadService;
        this.videoUtil = videoUtil;
    }

    @PostMapping("/videos")
    public ResponseEntity<FileUploadResponse> uploadVideo(@RequestParam("file") MultipartFile file) throws IOException, InvalidFileFormatException, InterruptedException {
        String filePath = uploadService.uploadFile(file);
        String duration = videoUtil.getVideoDuration(filePath);
        String formatDuration = videoUtil.formatDuration(duration);
        log.info("Video file path: [{}], duration: [{}]", filePath, duration);
        List<String> thumbnails = videoUtil.generateThumbnails(filePath, duration);
        log.info("File uploaded successfully. File absolute Path: [{}]", filePath);
        String videoFileRequestUrl = CommonUtil.INSTANCE.getFileRequestUrl(filePath);
        log.info("File uploaded successfully. Video File Request URL: [{}]", filePath);

        return ResponseEntity.ok(
                FileUploadResponse.builder()
                        .filePath(videoFileRequestUrl)
                        .duration(formatDuration)
                        .thumbnailImagePathList(thumbnails)
                        .build()
        );
    }

    @GetMapping("testMyPath")
    public String testContextPath() {
        return Paths.get(UPLOAD_DIR_RELATIVIZE).relativize(Paths.get(UPLOAD_DIR)).normalize().toString();
//        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toString();
    }

}
