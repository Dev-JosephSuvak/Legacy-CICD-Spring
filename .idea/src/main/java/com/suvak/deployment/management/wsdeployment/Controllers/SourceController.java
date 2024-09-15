package com.suvak.deployment.management.wsdeployment.Controllers;

import com.suvak.deployment.management.wsdeployment.Components.Facilitation.MoveFiles;
import com.suvak.deployment.management.wsdeployment.Entities.AngularRequest;
import com.suvak.deployment.management.wsdeployment.Entities.RepoRequest;
import com.suvak.deployment.management.wsdeployment.Service.RepoService;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/repo")
public class SourceController {


    // Define base URLs for SVN and GitLab

    @Value("${svn.host}")
    private static final String SVN_BASE_URL = "https://svn.com/svn/";
    @Value("${git.host}")
    private static final String GIT_BASE_URL = "https://github.com/";

    @Autowired
    private RepoService repoService;

    MoveFiles fileMover = new MoveFiles();

    @Value("${svn.path}")
    private String svnExecutablePath;

    private String repositoryPath;

    // Endpoint for SVN repository checkout and build process
    @PostMapping("/svn")
    public String checkoutSvnRepository(@RequestBody RepoRequest repoRequest) {
        String fullSvnUrl = SVN_BASE_URL + repoRequest.getRepoUrl();
        String svnName = repoRequest.getProjectName();
        String localPath = "C:/deployments/"+ repoRequest.getRepoUrl();

        try {
            // Call the service to check out the SVN repository
            repositoryPath = repoService.checkoutSvnRepo(fullSvnUrl, localPath, repoRequest.getProjectName(), repoRequest.getVirtualHost());
            // Package and move files (WAR and EAR)
            fileMover.moveFilesToDestination(repositoryPath, repoRequest.getDestinationFolder(), svnName);

            return "SVN repository - " + repoRequest.getProjectName() + " - built and deployed successfully.";
        } catch (IOException | InterruptedException e) {
            return "Error during SVN Build Process: " + e.getMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Endpoint for Git repository cloning and build process
    @PostMapping("/git")
    public String packageGitRepository(@RequestBody RepoRequest req) {
        String fullRepoUrl = GIT_BASE_URL + req.getProjectName() + ".git";
        String localPath = System.getProperty("java.io.tmpdir") + File.separator + req.getProjectName();

        try {
            // Clone Git repository
            repoService.checkoutGitRepo(fullRepoUrl, localPath, req.getProjectName(), req.getVirtualHost());

            // Package and move files (WAR and EAR)
            fileMover.moveFilesToDestination(localPath, req.getDestinationFolder(), req.getProjectName());

            return "Repository - " + req.getProjectName() + " - deployed successfully.";
        } catch (GitAPIException | IOException e) {
            return "Error during packaging of Git repository: " + e.getMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Endpoint for SVN repository checkout and build process
    @PostMapping("/angular/svn")
    public String checkoutAngSvnRepository(@RequestBody AngularRequest req) {
        String fullSvnAngUrl = SVN_BASE_URL + req.getAngularRepoUrl();
        String fullSvnUrl = SVN_BASE_URL + req.getServiceRepoUrl();
        String svnName = req.getProjectName();
        String localPath = "C:/deployments/"+ req.getServiceRepoUrl();

        try {

            // Call the service to check out the SVN repository
            repositoryPath = repoService.packageAngularRepo(fullSvnAngUrl, fullSvnUrl, localPath,
                    req.getVirtualHost(), req.getProjectName(), req.getAngularProjectName(), req.getAngularVersion());

            // Call the service to check out the Angular repository

            // Package and move files (WAR and EAR)
            fileMover.moveFilesToDestination(repositoryPath, req.getDestinationFolder(), svnName);

            return "SVN repository - " + req.getProjectName() + " - built and deployed successfully.";
        } catch (Exception e) {
            return "Error during SVN Build Process: " + e.getMessage();
        }
    }


    // Endpoint for SVN repository checkout and build process
    @PostMapping("/angular/git")
    public String checkoutAngGitRepository(@RequestBody AngularRequest req) {
        String fullGitUrl = GIT_BASE_URL + req.getServiceRepoUrl();
        String fullAngGitUrl = GIT_BASE_URL + req.getAngularRepoUrl();
        String gitName = req.getProjectName();
        String localPath = req.getDestinationFolder();

        try {
            // Call the service to check out the Git repository
            repositoryPath = repoService.packageAngularGitRepo(fullAngGitUrl, fullGitUrl, localPath,
                    req.getVirtualHost(), req.getProjectName(), req.getAngularProjectName(), req.getAngularVersion());

            // Package and move files (WAR and EAR)
            fileMover.packageAndMoveFiles(repositoryPath, req.getDestinationFolder(), gitName);

            return "Git repository - " + req.getProjectName() + " - built and deployed successfully.";
        } catch (IOException | InterruptedException e) {
            return "Error during Git Build Process: " + e.getMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
