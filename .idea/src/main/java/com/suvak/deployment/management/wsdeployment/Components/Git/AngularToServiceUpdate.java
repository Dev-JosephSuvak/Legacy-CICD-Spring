package com.suvak.deployment.management.wsdeployment.Components.Git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AngularToServiceUpdate {
    @Value("${git.username}")
    private String gitUsername;

    @Value("${git.password}")
    private String gitPassword;

    // Method to commit changes to Git with dynamic build datetime in the format 'Build - YYYYMMDDHHMM'
    public void commitToGit(String localPath) throws IOException {
        // Format current datetime for commit message in the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        String commitMessage = "Build - " + formattedDateTime;

        try (Git git = Git.open(new File(localPath))) {
            git.add().addFilepattern(".").call();  // Stage all changes
            git.commit().setMessage(commitMessage).call();  // Commit changes with dynamic timestamp
            git.push()
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitUsername, gitPassword))
                    .call();  // Push changes to the remote
            System.out.println("Changes committed and pushed to Git with commit message: " + commitMessage);
        } catch (GitAPIException e) {
            throw new IOException("Error during Git commit or push operation: " + e.getMessage(), e);
        }
    }
}
