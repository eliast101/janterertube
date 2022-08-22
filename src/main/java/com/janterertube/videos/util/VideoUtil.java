package com.janterertube.videos.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.janterertube.videos.constants.AppConstant.THUMBNAILS_DIR;
import static com.janterertube.videos.constants.AppConstant.UPLOAD_DIR_RELATIVIZE;

@Component
@Slf4j
public class VideoUtil {

    public static final String TIME_DELIMITER = ":";

    public static final String CMD_DIR = "C:\\Development\\ffmpeg\\bin\\";
    public static final String FFMPEG_SCRIPT = "ffmpeg.exe";
    public static final String FFPROBE_SCRIPT = "ffprobe";

    public String getVideoDuration(String filePath) throws IOException, InterruptedException {
        String command = CMD_DIR + FFPROBE_SCRIPT + " -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 " + filePath;
        Process process = Runtime.getRuntime().exec(command);
        int exitStatus = process.waitFor();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        String output = StringUtils.EMPTY;
        while ((line = reader.readLine()) != null) {
            output = line;
            System.out.println(output);
        }
        if (exitStatus != 0) {
            reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = reader.readLine()) != null) {
                System.err.println(line);
            }
            throw new IllegalStateException("Script exited abnormally.");
        }
        System.out.println("******** Script executed successfully *********");
        if (StringUtils.isBlank(output)) {
            throw new IOException("Unable to read duration of video file [" + filePath + "]");
        }
        return output;
    }

    public List<String> generateThumbnails(String filePath, String duration) throws IOException, InterruptedException {
        int durationInDouble = Integer.parseInt(duration.substring(0, duration.indexOf(".")));
        String thumbnailSize = "210x118";
        int numThumbnails = 3;

        List<String> thumbnailPathList = new ArrayList<>();
        for (int i = 1; i < numThumbnails + 1; i++) {
            String imageName = UUID.randomUUID() + ".jpg";
            int interval = (int) ((durationInDouble * 0.8) / numThumbnails * i);
            String fullThumbnailPath = THUMBNAILS_DIR + imageName;

//            String command = CMD_DIR + FFMPEG_SCRIPT + " -ss " + interval + " -i " + interval + " -s " + thumbnailSize + " -vframes 1 " + fullThumbnailPath;
            String command = CMD_DIR + FFMPEG_SCRIPT + " -ss " + interval + " -i " + filePath +  " -s " + thumbnailSize +  " -vframes 1 " + fullThumbnailPath;
            //System.out.println(command);
            Process process = Runtime.getRuntime().exec(command);
            String thumbnailContextPath = CommonUtil.INSTANCE.getFileRequestUrl(fullThumbnailPath);
            thumbnailPathList.add(thumbnailContextPath);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
        return thumbnailPathList;
    }

    /*public String formatDuration(String output) {
        String[] elements = output.split(TIME_DELIMITER);
        System.out.println(Arrays.toString(elements));
        String hours = elements[0];
        String minutes = elements[1];
        String seconds = elements[2].substring(0, elements[2].indexOf("."));

        return hours.equals("0") ? (minutes + TIME_DELIMITER + seconds) : (hours + TIME_DELIMITER + minutes + TIME_DELIMITER + seconds);
    }*/

    public String formatDuration(String output) {
        int durationInSec = Integer.parseInt(output.substring(0, output.indexOf(".")));
        int hoursInt = durationInSec / 3600;
        int minutesInt = (durationInSec -(hoursInt*3600)) / 60;
        int secondsInt = durationInSec % 60;

        String hours = (hoursInt < 1) ? "" : (hoursInt + TIME_DELIMITER);
        String minutes = (minutesInt < 10) ? ("0" + minutesInt + TIME_DELIMITER): (minutesInt + TIME_DELIMITER);
        String seconds = (secondsInt < 10) ? ("0" + secondsInt): String.valueOf(secondsInt);

        return hours + minutes + seconds;
    }
}
