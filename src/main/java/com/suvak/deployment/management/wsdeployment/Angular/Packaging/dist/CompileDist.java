package com.suvak.deployment.management.wsdeployment.Angular.Packaging.dist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CompileDist {

    public static void generateDist(String angularPath, String contextRoot, String angularVersion) throws IOException {
        // Ensure the contextRoot is properly formatted with leading and trailing slashes
        if (!contextRoot.startsWith("/")) {
            contextRoot = "/" + contextRoot;
        }
        if (!contextRoot.endsWith("/")) {
            contextRoot = contextRoot + "/";
        }

        // Commands to execute
        String useNodeCommand = "nvm use " + angularVersion;
        String npmInstallCommand = "npm install";
        String installPrimeNgCommand = "npm install primeng --save";
        String buildCommand = "npm run ng build --base-href=" + contextRoot + " --prod --omit=dev";

        // Execute each command in sequence
        runCommand(angularPath, useNodeCommand);
        runCommand(angularPath, npmInstallCommand);
        runCommand(angularPath, installPrimeNgCommand);
        runCommand(angularPath, buildCommand);

        System.out.println("Angular dist generation process completed.");
    }

    private static void runCommand(String angularPath, String command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
        processBuilder.directory(new java.io.File(angularPath));

        // Start the process
        Process process = processBuilder.start();

        // Capture the output and error streams
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        String line;
        System.out.println("Running command: " + command);
        System.out.println("Output:");
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        System.out.println("Errors (if any):");
        while ((line = errorReader.readLine()) != null) {
            System.err.println(line);
        }

        try {
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Command '" + command + "' executed successfully.");
            } else {
                System.err.println("Command '" + command + "' failed with exit code: " + exitCode);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Process was interrupted", e);
        }
    }
}
