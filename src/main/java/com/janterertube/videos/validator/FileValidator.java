package com.janterertube.videos.validator;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public enum FileValidator {

    INSTANCE;

    public boolean isValidateFile(FileValidationType type, MultipartFile file) {
        String filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (type == FileValidationType.VIDEO) {
            return ValidFileFormats.VALID_VIDEO_FORMATS.contains(filenameExtension);
        }
        //TODO: condition for pictures
        return false;
    }
}
