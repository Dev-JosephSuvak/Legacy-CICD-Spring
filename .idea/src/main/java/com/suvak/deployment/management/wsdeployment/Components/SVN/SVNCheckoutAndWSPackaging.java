package com.suvak.deployment.management.wsdeployment.Components.SVN;

import com.suvak.deployment.management.wsdeployment.Angular.Implementation.AngularImpl;
import com.suvak.deployment.management.wsdeployment.Angular.Interface.Angular;
import com.suvak.deployment.management.wsdeployment.WebSphere.Implementation.WebSphereImpl;
import com.suvak.deployment.management.wsdeployment.WebSphere.Interface.WebSphere;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SVNCheckoutAndWSPackaging {
    WebSphere webSphereFileGenerator = new WebSphereImpl();
    Angular angularCompiler = new AngularImpl();
    AngularToServiceUpdate angular = new AngularToServiceUpdate();

    @Value("${svn.path}")
    private String svnExecutablePath;

    // Overloaded method without optional parameters (useful for non-Angular repositories)
    public String checkoutSvnRepo(String svnUrl, String localPath, String contextRoot, String virtual_host) throws Exception {
        return checkoutSvnRepo(svnUrl, localPath, contextRoot, virtual_host, false, null, null, null);
    }


    public String checkoutSvnRepo(String svnUrl, String localPath, String contextRoot, String virtual_host, Boolean isAngular, String angularSvnUrl, String angularName, String npmVersion) throws Exception {
        // Log the paths and URLs for debugging
        System.out.println("SVN Executable Path: " + svnExecutablePath);
        System.out.println("SVN URL: " + svnUrl);
        System.out.println("Local Path: " + localPath);

        // Create the ProcessBuilder with the svn.exe path
        ProcessBuilder processBuilder = new ProcessBuilder(svnExecutablePath, "checkout", svnUrl, localPath);

        // Redirect errors to the main output stream
        processBuilder.redirectErrorStream(true);

        // Start the process and capture output
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        int exitCode = process.waitFor();

        // If the checkout fails due to a previous incomplete operation, run 'svn cleanup'
        if (output.toString().contains("E155037")) {
            System.out.println("Detected incomplete operation. Running 'svn cleanup'...");

            // Run the cleanup command
            ProcessBuilder cleanupProcessBuilder = new ProcessBuilder(svnExecutablePath, "cleanup", localPath);
            cleanupProcessBuilder.redirectErrorStream(true);
            Process cleanupProcess = cleanupProcessBuilder.start();
            int cleanupExitCode = cleanupProcess.waitFor();

            if (cleanupExitCode == 0) {
                System.out.println("Cleanup successful. Retrying checkout...");

                // Retry the checkout after cleanup
                processBuilder = new ProcessBuilder(svnExecutablePath, "checkout", svnUrl, localPath);
                processBuilder.redirectErrorStream(true);
                process = processBuilder.start();
                exitCode = process.waitFor();
            } else {
                return "Error during cleanup, exit code: " + cleanupExitCode;
            }
        }

        // Capture error output
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        StringBuilder errorOutput = new StringBuilder();
        while ((line = errorReader.readLine()) != null) {
            errorOutput.append(line).append("\n");
        }

        // Log and return the output
        System.out.println("Process Output: " + output.toString());
        if (exitCode != 0) {
            System.err.println("Process Error Output: " + errorOutput.toString());
            return "Error during SVN checkout, exit code: " + exitCode + ". Error: " + errorOutput.toString();
        }

        System.out.println("SVN repository checked out to: " + localPath);
        webSphereFileGenerator.generateXmlFiles(localPath, contextRoot, virtual_host);
        webSphereFileGenerator.generateXmiFiles(localPath, contextRoot, virtual_host);
        webSphereFileGenerator.generateJARFiles(localPath+ ".classpath", localPath + "/WebContent/WEB-INF/lib");

        if (isAngular) {
            // Compile Angular UI
            angularCompiler.compileAngularUI(angularSvnUrl, contextRoot, angularName, npmVersion, svnUrl);
            // After compiling Angular UI, re-commit the changes to SVN
            angular.commitToSvn(localPath, svnUrl);
        }

        return localPath;
    }
}