package com.javarush.task.task31.task3101;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Map;
import java.util.TreeMap;

/* 
Проход по дереву файлов
*/

public class Solution {
    public static void main(String[] args) throws IOException {
        File file = new File(args[1]);
        File renamingFile = new File(file.getParent() + "/allFilesContent.txt");

        FileUtils.renameFile(file, renamingFile);

        MyVisitor myVisitor = new MyVisitor();
        Files.walkFileTree(Paths.get(args[0]), myVisitor);

        Map<String, File> map = myVisitor.getMap();

        try (FileOutputStream fos = new FileOutputStream(renamingFile)) {
            map.forEach((k, v) -> {
                try {
                    byte[] buffer = Files.readAllBytes(v.toPath());
                    fos.write(buffer);
                    fos.write("\n".getBytes());
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });
        }

    }

    private static class MyVisitor extends SimpleFileVisitor<Path> {
        private Map<String, File> map = new TreeMap<>();

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (!Files.isDirectory(file) &&
                        file.getFileName().toString().endsWith(".txt") &&
                        file.toFile().length() <= 50) {
                    map.put(file.getFileName().toString(), file.toFile());
                }

            return FileVisitResult.CONTINUE;
        }

        public Map<String, File> getMap() {
            return map;
        }

    }
}





