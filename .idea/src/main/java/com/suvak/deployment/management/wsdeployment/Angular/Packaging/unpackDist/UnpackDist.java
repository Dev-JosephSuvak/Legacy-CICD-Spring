package com.suvak.deployment.management.wsdeployment.Angular.Packaging.unpackDist;

import java.io.IOException;
import java.nio.file.*;

public class UnpackDist {

    // Method to unpack the dist folder and move two levels down to content and drop it in the serviceRepo + WebContent
    public static void unpackDist(String distPath, String folderName, String serviceRepo) throws IOException {
        // Navigate two folders down from dist (e.g., dist/{folderName}/content)
        Path distContentPath = Paths.get(distPath).resolve(folderName + "/content");

        // Target path in the serviceRepo + WebContent
        Path targetPath = Paths.get(serviceRepo, "WebContent");

        // Ensure the target directory exists
        if (!Files.exists(targetPath)) {
            Files.createDirectories(targetPath);
        }

        // Copy the contents of distContentPath to targetPath
        Files.walk(distContentPath).forEach(source -> {
            Path destination = targetPath.resolve(distContentPath.relativize(source));
            try {
                if (Files.isDirectory(source)) {
                    if (!Files.exists(destination)) {
                        Files.createDirectory(destination);
                    }
                } else {
                    Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                System.err.println("Failed to copy file: " + source.toString() + " -> " + destination.toString());
                e.printStackTrace();
            }
        });

        System.out.println("Unpacking of dist files is completed.");
    }
}
