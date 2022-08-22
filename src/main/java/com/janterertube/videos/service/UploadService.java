package com.janterertube.videos.service;

import com.janterertube.videos.exceptions.InvalidFileFormatException;
import com.janterertube.videos.validator.FileValidationType;
import com.janterertube.videos.validator.FileValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static com.janterertube.videos.constants.AppConstant.UPLOAD_DIR;
import static com.janterertube.videos.validator.ValidFileFormats.VALID_VIDEO_FORMATS;

@Service
public class UploadService {

    public String uploadFile(MultipartFile file) throws IOException, InvalidFileFormatException {
        String filePathString = "";
        if (FileValidator.INSTANCE.isValidateFile(FileValidationType.VIDEO, file)) {
            Path filePath = Paths.get(UPLOAD_DIR + UUID.randomUUID() + "_" + replaceWhiteSpace(file.getOriginalFilename()));
            file.transferTo(filePath);
            filePathString = filePath.toString();
        } else {
            throw new InvalidFileFormatException("Invalid file format. Valid video types are " + VALID_VIDEO_FORMATS.toString());
        }
        return filePathString;
    }

    private String replaceWhiteSpace(String fileName) {
        return StringUtils.replace(fileName, StringUtils.SPACE, "_");
    }


}
