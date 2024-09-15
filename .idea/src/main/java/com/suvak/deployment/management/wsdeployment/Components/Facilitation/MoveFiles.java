package com.suvak.deployment.management.wsdeployment.Components.Facilitation;

import com.suvak.deployment.management.wsdeployment.Components.Compilation.PackageEAR;
import com.suvak.deployment.management.wsdeployment.Components.Compilation.PackageWAR;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MoveFiles {

    PackageWAR war = new PackageWAR();
    PackageEAR ear = new PackageEAR();

    public void packageAndMoveFiles(String localPath, String destinationFolder, String projectName) throws IOException {
        // Generate the WAR and EAR file names using system-specific file separator
        String warFile = localPath + File.separator + projectName + ".war";
        String earFile = localPath + File.separator + projectName + ".ear";

        // Package the repository as WAR and EAR
        war.packageWar(localPath, warFile);
        ear.packageEar(warFile, earFile);

        // Move the packaged WAR and EAR files to the destination folder
        moveFilesToDestination(warFile, earFile, destinationFolder);
    }
    public void moveFilesToDestination(String warFile, String earFile, String destinationFolder) throws IOException {
        File war = new File(warFile);
        File ear = new File(earFile);

        // Extract the base name of the WAR and EAR files (e.g., "MEPQR" from "MEPQR.war")
        String baseName = war.getName().replace(".war", "");

        // Create a subdirectory for this specific WAR/EAR file in the destination folder
        File destDir = new File(destinationFolder, baseName);
        if (!destDir.exists()) {
            destDir.mkdirs(); // Create the directory if it does not exist
        }

        // Create a timestamp for archiving
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        // Handle WAR file
        File warDest = new File(destDir, war.getName());
        if (warDest.exists()) {
            // Archive existing WAR file by renaming it with a timestamp
            File archivedWar = new File(destDir, war.getName().replace(".war", "_" + timestamp + ".war"));
            if (!warDest.renameTo(archivedWar)) {
                throw new IOException("Failed to archive existing WAR file at: " + warDest.getPath());
            }
            System.out.println("Archived WAR to: " + archivedWar.getPath());
        }
        // Move the new WAR file to the original location
        if (war.renameTo(warDest)) {
            System.out.println("Moved new WAR to: " + warDest.getPath());
        } else {
            throw new IOException("Failed to move new WAR file to: " + warDest.getPath());
        }

        // Handle EAR file
        File earDest = new File(destDir, ear.getName());
        if (earDest.exists()) {
            // Archive existing EAR file by renaming it with a timestamp
            File archivedEar = new File(destDir, ear.getName().replace(".ear", "_" + timestamp + ".ear"));
            if (!earDest.renameTo(archivedEar)) {
                throw new IOException("Failed to archive existing EAR file at: " + earDest.getPath());
            }
            System.out.println("Archived EAR to: " + archivedEar.getPath());
        }
        // Move the new EAR file to the original location
        if (ear.renameTo(earDest)) {
            System.out.println("Moved new EAR to: " + earDest.getPath());
        } else {
            throw new IOException("Failed to move new EAR file to: " + earDest.getPath());
        }
    }

}
