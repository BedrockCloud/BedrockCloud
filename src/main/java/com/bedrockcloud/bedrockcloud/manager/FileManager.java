package com.bedrockcloud.bedrockcloud.manager;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.config.FileUtils;

import java.io.*;

public class FileManager {

    public static int getFreeNumber(final String path) {
        boolean found = false;
        int i = 1;
        while (!found) {
            final File file = new File(path + "-" + i);
            if (!file.exists()) {
                found = true;
                --i;
            }
            ++i;
        }
        return i;
    }

    public static void copy(final File src, final File dest) {
        try {
            if (src.isDirectory()) {
                if (!dest.exists()) {
                    dest.mkdir();
                }
                final String[] list;
                final String[] files = list = src.list();
                for (final String file : list) {
                    final File srcFile = new File(src, file);
                    final File destFile = new File(dest, file);
                    copy(srcFile, destFile);
                }
            } else {
                InputStream in = null;
                try {
                    in = new FileInputStream(src);
                    final OutputStream out = new FileOutputStream(dest);
                    final byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    in.close();
                    out.close();
                } catch (IOException e) {
                    BedrockCloud.getLogger().exception(e);
                }
            }
        } catch (Exception ignored) {}
    }

    public static void deleteServer(final File file, final String serverName, boolean isStatic) {
        try {
            final File Crashfile = new File("./temp/" + serverName + "/crashdumps/");
            final File dest_lib = new File("./archive/crashdumps/" + serverName + "/");
            dest_lib.mkdirs();
            FileManager.copy(Crashfile, dest_lib);
            FileUtils.copyFile(Crashfile, dest_lib);
        } catch (IOException e) {
            //e.printStackTrace();
        }

        try {
            if (!isStatic) {
                if (file.isDirectory()) {
                    final String[] fileList = file.list();
                    if (fileList.length == 0) {
                        file.delete();
                    } else {
                        for (final String fileName : fileList) {
                            final String fullPath = file.getPath() + "/" + fileName;
                            final File fileOrFolder = new File(fullPath);
                            delete(fileOrFolder);
                        }
                        delete(file);
                    }
                } else {
                    file.delete();
                }
            }
        } catch (NullPointerException ignored){}
    }

    public static void delete(final File file) {
        try {
            if (file.isDirectory()) {
                final String[] fileList = file.list();
                if (fileList.length == 0) {
                    file.delete();
                } else {
                    for (final String fileName : fileList) {
                        final String fullPath = file.getPath() + "/" + fileName;
                        final File fileOrFolder = new File(fullPath);
                        delete(fileOrFolder);
                    }
                    delete(file);
                }
            } else {
                file.delete();
            }
        } catch (NullPointerException ignored){}
    }
}
