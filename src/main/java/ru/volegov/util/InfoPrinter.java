package ru.volegov.util;

import org.apache.maven.model.Model;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class InfoPrinter {
    public static void printInfo() {
        InputStream in = Model.class.getClassLoader().getResourceAsStream("info.txt");
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (in, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
            in.close();
            System.out.println("\n\nInfo\n========================\n");
            System.out.println(textBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
