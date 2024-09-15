package com.suvak.deployment.management.wsdeployment.Components.SVN;

import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AngularToServiceUpdate {

    @Value("${svn.path}")
    private String svnExecutablePath;
    public void commitToSvn(String localPath, String serviceSVNUrl) throws IOException, InterruptedException {
        // Add any new or modified files to SVN
        System.out.println("Running SVN add...");
        ProcessBuilder addProcessBuilder = new ProcessBuilder(svnExecutablePath, "add", "--force", localPath);
        addProcessBuilder.redirectErrorStream(true);
        Process addProcess = addProcessBuilder.start();
        addProcess.waitFor();

        // Capture any errors during the 'add' process
        BufferedReader addErrorReader = new BufferedReader(new InputStreamReader(addProcess.getErrorStream()));
        StringBuilder addErrorOutput = new StringBuilder();
        String addLine;
        while ((addLine = addErrorReader.readLine()) != null) {
            addErrorOutput.append(addLine).append("\n");
        }

        // Commit the changes to SVN
        System.out.println("Committing changes to SVN...");
        ProcessBuilder commitProcessBuilder = new ProcessBuilder(svnExecutablePath, "commit", "-m", "Updated Angular repo after build", localPath);
        commitProcessBuilder.redirectErrorStream(true);
        Process commitProcess = commitProcessBuilder.start();

        BufferedReader commitReader = new BufferedReader(new InputStreamReader(commitProcess.getInputStream()));
        StringBuilder commitOutput = new StringBuilder();
        String commitLine;
        while ((commitLine = commitReader.readLine()) != null) {
            commitOutput.append(commitLine).append("\n");
        }

        int commitExitCode = commitProcess.waitFor();

        if (commitExitCode == 0) {
            System.out.println("Commit successful.");
        } else {
            System.err.println("Error during SVN commit: " + commitOutput.toString());
        }
    }

}
