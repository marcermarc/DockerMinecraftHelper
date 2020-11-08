package de.marcermarc.mchelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Util {
    private Util() {
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        return str.chars().allMatch(Character::isDigit);
    }

    //region download
    public static String downloadString(String source) throws IOException {
        URL sourceUrl = new URL(source);

        try (ReadableByteChannel readableByteChannel = Channels.newChannel(sourceUrl.openStream())) {
            try (Scanner scanner = new Scanner(readableByteChannel)) {
                scanner.useDelimiter("\\Z");
                return scanner.next();
            }
        }
    }

    public static void downloadFile(String source, String destination) throws IOException {
        URL sourceUrl = new URL(source);

        try (ReadableByteChannel readableByteChannel = Channels.newChannel(sourceUrl.openStream())) {
            try (FileChannel fileChannel = new FileOutputStream(destination).getChannel()) {
                fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            }
        }
    }

    public static void downloadFile(String source, Path destination) throws IOException {
        downloadFile(source, destination.toString());
    }

    /**
     * Download the zip and extract the first file from the zip to the destination Path
     * <p>
     * Inspired by https://gist.github.com/soberich/eb3b98e5b5c91b56fe711f4782ccf119
     */
    public static void downloadAndExtractFile(final String source, final Path destination) throws IOException {
        final URL url = new URL(source);

        try (ZipInputStream zipInputStream = new ZipInputStream(Channels.newInputStream(Channels.newChannel(url.openStream())))) {
            ZipEntry entry;
            do {
                entry = zipInputStream.getNextEntry();
            } while (entry.isDirectory());

            if (!Files.exists(destination.getParent())) {
                Files.createDirectory(destination.getParent());
            }

            try (FileChannel fileChannel = FileChannel.open(destination, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
                fileChannel.transferFrom(Channels.newChannel(zipInputStream), 0, Long.MAX_VALUE);
            }
        }
    }
//endregion

    /**
     * source: https://softwarecave.org/2018/03/24/delete-directory-with-contents-in-java/#:~:text=Removing%20empty%20directory%20in%20Java,will%20refuse%20to%20remove%20it.
     */
    public static void deleteDirectory(Path path) throws IOException {
        if (!Files.exists(path)) {
            return;
        }

        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    public static int compareVersions(final String x, final String y, final String splitRegex) {
        if (x.equals(y)) {
            return 0;
        }

        String[] xSplit = x.split(splitRegex);
        String[] ySplit = y.split(splitRegex);

        int maxLength = Math.max(xSplit.length, ySplit.length);
        for (int i = 0; i < maxLength; i++) {
            if (xSplit.length <= i) {
                return -Integer.parseInt(ySplit[i]);
            } else if (ySplit.length <= i) {
                return Integer.parseInt(xSplit[i]);
            }

            int xValue = Integer.parseInt(xSplit[i]);
            int yValue = Integer.parseInt(ySplit[i]);

            if (xValue != yValue) {
                return Integer.compare(xValue, yValue) * (int) Math.pow(10, maxLength - i);
            }
        }
        return 0;
    }
}
