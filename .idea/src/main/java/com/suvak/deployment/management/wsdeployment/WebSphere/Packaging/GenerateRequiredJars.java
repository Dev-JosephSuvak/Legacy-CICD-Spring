package com.suvak.deployment.management.wsdeployment.WebSphere.Packaging;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class GenerateRequiredJars {

    private String targetClasspathFilePath;  // Path to the target repository's .classpath file
    private String targetWebInfLibPath;      // Path to the target repository's WebContent/WEB-INF/lib
    private String deploymentLibPath;        // Path to the deployment application's WEB-INF/lib

    // Constructor to initialize the paths
    public GenerateRequiredJars(String targetClasspathFilePath, String targetWebInfLibPath, String deploymentLibPath) {
        this.targetClasspathFilePath = targetClasspathFilePath;
        this.targetWebInfLibPath = targetWebInfLibPath;
        this.deploymentLibPath = deploymentLibPath;
    }

    // Method to process JARs: Scan .classpath in the target repo, and move matching JARs
    public void processJars() {
        try {
            File classpathFile = new File(targetClasspathFilePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(classpathFile);

            NodeList nodeList = doc.getElementsByTagName("classpathentry");

            System.out.println("Scanning target .classpath for JAR files...");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String kind = element.getAttribute("kind");
                String jarPath = element.getAttribute("path");

                // Only consider "lib" entries, which indicate JAR files
                if ("lib".equals(kind)) {
                    System.out.println("Found JAR in target classpath: " + jarPath);

                    // Extract the name of the JAR file from the path
                    String jarFileName = new File(jarPath).getName();

                    // Try to find the matching JAR in the deployment application's lib directory
                    moveMatchingJarToTarget(jarFileName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to move a matching JAR file from the deployment lib to the target WEB-INF/lib directory
    private void moveMatchingJarToTarget(String jarFileName) {
        File deploymentLibDirectory = new File(deploymentLibPath);
        File[] deploymentJars = deploymentLibDirectory.listFiles((dir, name) -> name.equals(jarFileName));

        if (deploymentJars != null && deploymentJars.length > 0) {
            File jarFile = deploymentJars[0];  // Found the matching JAR in deployment lib

            File targetLibDirectory = new File(targetWebInfLibPath);
            if (!targetLibDirectory.exists()) {
                targetLibDirectory.mkdirs();  // Create the directory if it doesn't exist
            }

            File targetJarFile = new File(targetLibDirectory, jarFile.getName());

            try {
                // Copy the JAR file from deployment to the target repository (overwrite if exists)
                Files.copy(jarFile.toPath(), targetJarFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Moved " + jarFile.getName() + " to " + targetJarFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error moving JAR: " + jarFile.getName());
                e.printStackTrace();
            }
        } else {
            System.out.println("No matching JAR found for " + jarFileName + " in the deployment lib.");
        }
    }
}
