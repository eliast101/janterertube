package com.janterertube.videos.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Paths;

import static com.janterertube.videos.constants.AppConstant.UPLOAD_DIR_RELATIVIZE;

public enum CommonUtil {

    INSTANCE;

    public String getFileRequestUrl(String filePath) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build().toString() + "/" +
                Paths.get(UPLOAD_DIR_RELATIVIZE).relativize(Paths.get(filePath)).toString().replace("\\", "/");
    }
}
