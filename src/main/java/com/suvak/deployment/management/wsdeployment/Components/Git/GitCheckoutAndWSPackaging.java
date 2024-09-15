package com.suvak.deployment.management.wsdeployment.Components.Git;

import com.suvak.deployment.management.wsdeployment.Angular.Implementation.AngularImpl;
import com.suvak.deployment.management.wsdeployment.Angular.Interface.Angular;
import com.suvak.deployment.management.wsdeployment.WebSphere.Implementation.WebSphereImpl;
import com.suvak.deployment.management.wsdeployment.WebSphere.Interface.WebSphere;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;

public class GitCheckoutAndWSPackaging {
    WebSphere webSphereFileGenerator = new WebSphereImpl();
    Angular angularCompiler = new AngularImpl();
    AngularToServiceUpdate angular = new AngularToServiceUpdate();



    @Value("${git.username}")
    private String gitUsername;

    @Value("${git.password}")
    private String gitPassword;

    // Overloaded method without optional parameters (useful for non-Angular repositories)
    public String checkoutGitRepo(String gitUrl, String localPath, String contextRoot, String virtualHost) throws Exception {
        return checkoutGitRepo(gitUrl, localPath, contextRoot, virtualHost, false, null, null, null);
    }

    // Main method to clone the repository, optionally compile Angular, and package it for WebSphere
    public String checkoutGitRepo(String gitUrl, String localPath, String contextRoot, String virtualHost, Boolean isAngular, String angularGitUrl, String angularName, String npmVersion) throws Exception {
        // Log the paths and URLs for debugging
        System.out.println("Git URL: " + gitUrl);
        System.out.println("Local Path: " + localPath);

        // Clone the Git repository
        try {
            Git.cloneRepository()
                    .setURI(gitUrl)
                    .setDirectory(new File(localPath))
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitUsername, gitPassword))
                    .call();
            System.out.println("Git repository cloned to: " + localPath);
        } catch (GitAPIException e) {
            throw new IOException("Error during Git clone operation: " + e.getMessage(), e);
        }

        // Generate WebSphere files (common functionality)
        webSphereFileGenerator.generateXmlFiles(localPath, contextRoot, virtualHost);
        webSphereFileGenerator.generateXmiFiles(localPath, contextRoot, virtualHost);
        webSphereFileGenerator.generateJARFiles(localPath + ".classpath", localPath + "/WebContent/WEB-INF/lib");

        if (isAngular != null && isAngular) {
            // Compile Angular UI
            angularCompiler.compileAngularUI(angularGitUrl, contextRoot, angularName, npmVersion, gitUrl);

            // After compiling Angular UI, commit changes back to Git
            angular.commitToGit(localPath);
        }

        return localPath;
    }

}
