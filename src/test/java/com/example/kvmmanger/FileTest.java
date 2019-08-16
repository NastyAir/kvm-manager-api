package com.example.kvmmanger;

import java.io.File;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FileTest {
    public static void main(String[] args) {

        File file=new File("v:/token/"+ UUID.randomUUID());
        List<String> lines = Arrays.asList("The first line", "The second line");
        try {
            Files.write(file.toPath(), lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
