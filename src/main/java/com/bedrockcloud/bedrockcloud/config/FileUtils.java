package com.bedrockcloud.bedrockcloud.config;

import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class FileUtils
{
    public static void writeFile(final String fileName, final String content) throws IOException {
        writeFile(fileName, new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
    }
    
    public static void writeFile(final String fileName, final InputStream content) throws IOException {
        writeFile(new File(fileName), content);
    }
    
    public static void writeFile(final File file, final String content) throws IOException {
        writeFile(file, new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
    }
    
    public static void writeFile(final File file, final InputStream content) throws IOException {
        if (content == null) {
            throw new IllegalArgumentException("content must not be null");
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        final FileOutputStream stream = new FileOutputStream(file);
        final byte[] buffer = new byte[1024];
        int length;
        while ((length = content.read(buffer)) != -1) {
            stream.write(buffer, 0, length);
        }
        stream.close();
        content.close();
    }
    
    public static String readFile(final File file) throws IOException {
        if (!file.exists() || file.isDirectory()) {
            throw new FileNotFoundException();
        }
        return readFile(new FileInputStream(file));
    }
    
    public static String readFile(final String filename) throws IOException {
        final File file = new File(filename);
        if (!file.exists() || file.isDirectory()) {
            throw new FileNotFoundException();
        }
        return readFile(new FileInputStream(file));
    }
    
    public static String readFile(final InputStream inputStream) throws IOException {
        return readFile(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }
    
    private static String readFile(final Reader reader) throws IOException {
        final BufferedReader br = new BufferedReader(reader);
        final StringBuilder stringBuilder = new StringBuilder();
        for (String temp = br.readLine(); temp != null; temp = br.readLine()) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append(temp);
        }
        br.close();
        reader.close();
        return stringBuilder.toString();
    }
    
    public static void copyFile(final File from, final File to) throws IOException {
        if (!from.exists()) {
            throw new FileNotFoundException();
        }
        if (from.isDirectory() || to.isDirectory()) {
            throw new FileNotFoundException();
        }
        FileInputStream fi = null;
        FileChannel in = null;
        FileOutputStream fo = null;
        FileChannel out = null;
        try {
            if (!to.exists()) {
                to.createNewFile();
            }
            fi = new FileInputStream(from);
            in = fi.getChannel();
            fo = new FileOutputStream(to);
            out = fo.getChannel();
            in.transferTo(0L, in.size(), out);
        }
        finally {
            if (fi != null) {
                fi.close();
            }
            if (in != null) {
                in.close();
            }
            if (fo != null) {
                fo.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
