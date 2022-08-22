package com.janterertube.videos.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class VideoUtilTest {

    private VideoUtil videoUtil;

    @BeforeEach
    void setUp() {
        videoUtil = new VideoUtil();
    }

    @Test
    void getVideoDurationTest() throws IOException, InterruptedException {
        String filePath = "C:\\Users\\elorh\\OneDrive\\Documents\\intellij-workspace\\youtube-clone\\VideoTubeAngular\\backend\\janterertube\\src\\main\\resources\\static\\uploads\\videos\\Rosalía_-_La_Llorona_live_-_Copy.mp4";
        String videoDuration = videoUtil.getVideoDuration(filePath);
        List<String> strings = videoUtil.generateThumbnails(filePath, videoDuration);
        System.out.println(strings);
    }

    @Test
    void testParallelStream() {
        Map<String, List<Integer>> myMap = new HashMap<>();
        List<Integer> myList1 = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
        List<Integer> myList2 = IntStream.rangeClosed(10001, 20000).boxed().collect(Collectors.toList());

        myMap.put("one", myList1);
        myMap.put("two", myList2);

        long start = 0;
        long end = 0;
        start = System.currentTimeMillis();
        myMap.entrySet().parallelStream().forEach(stringListEntry -> {

            stringListEntry.getValue().forEach(integer -> {
                try {
                    Files.writeString(Paths.get("C:\\Users\\elorh\\OneDrive\\Documents\\intellij-workspace\\youtube-clone\\VideoTubeAngular\\backend\\janterertube\\testParallelStream.txt"), String.valueOf(integer));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
        end = System.currentTimeMillis();
        System.out.println("ParallelStream took " + (end - start) + " milliseconds");


        System.out.println("==============================================================");

        start = System.currentTimeMillis();
        myMap.entrySet().forEach(stringListEntry -> {
            stringListEntry.getValue().forEach(integer -> {
                try {
                    Files.writeString(Paths.get("C:\\Users\\elorh\\OneDrive\\Documents\\intellij-workspace\\youtube-clone\\VideoTubeAngular\\backend\\janterertube\\testNormalStream.txt"), String.valueOf(integer));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
        end = System.currentTimeMillis();
        System.out.println("Stream took " + (end - start) + " milliseconds");
    }
}