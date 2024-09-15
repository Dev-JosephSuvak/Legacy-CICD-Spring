package com.suvak.deployment.management.wsdeployment.Service;

import com.suvak.deployment.management.wsdeployment.Angular.Implementation.AngularImpl;
import com.suvak.deployment.management.wsdeployment.Angular.Interface.Angular;
import com.suvak.deployment.management.wsdeployment.Components.Git.GitCheckoutAndWSPackaging;
import com.suvak.deployment.management.wsdeployment.Components.SVN.AngularToServiceUpdate;
import com.suvak.deployment.management.wsdeployment.Components.SVN.SVNCheckoutAndWSPackaging;
import com.suvak.deployment.management.wsdeployment.WebSphere.Implementation.WebSphereImpl;
import com.suvak.deployment.management.wsdeployment.WebSphere.Interface.WebSphere;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class RepoService {

    WebSphere webSphereFileGenerator = new WebSphereImpl();
    Angular angularCompiler = new AngularImpl();
    SVNCheckoutAndWSPackaging svn = new SVNCheckoutAndWSPackaging();
    GitCheckoutAndWSPackaging git = new GitCheckoutAndWSPackaging();

    AngularToServiceUpdate angular = new AngularToServiceUpdate();
    // Method to clone a Git repository
    public void cloneGitRepo(String gitUrl, String localPath) throws GitAPIException {
        Git.cloneRepository()
                .setURI(gitUrl)
                .setDirectory(new File(localPath))
                .call();
        System.out.println("Git repository cloned to: " + localPath);
    }
    // Inject SVN executable path from application.properties
     @Value("${svn.path}")
    private String svnExecutablePath;

    // Method to check out an SVN repository from SVN & compile it.
    public String checkoutSvnRepo(String svnUrl, String localPath, String contextRoot, String virtual_host) throws Exception {
        svn.checkoutSvnRepo(svnUrl,localPath,contextRoot,virtual_host);
    return localPath;
    }

    // Method to package an Angular repository to a Service repository & will update SVN
    public String packageAngularRepo(String angularSvnUrl, String serviceSvnUrl, String localPath,String virtual_host, String contextRoot, String angularName, String npmVersion) throws Exception {
        svn.checkoutSvnRepo(serviceSvnUrl, localPath, contextRoot, virtual_host, true, angularSvnUrl, angularName, npmVersion);
        return localPath;
    }

    public String checkoutGitRepo(String svnUrl, String localPath, String contextRoot, String virtual_host) throws Exception {
        git.checkoutGitRepo(svnUrl,localPath,contextRoot,virtual_host);
        return localPath;
    }

    // Method to package an Angular repository to a Service repository & will update SVN
    public String packageAngularGitRepo(String angularSvnUrl, String serviceSvnUrl, String localPath,String virtual_host, String contextRoot, String angularName, String npmVersion) throws Exception {
        git.checkoutGitRepo(serviceSvnUrl, localPath, contextRoot, virtual_host, true, angularSvnUrl, angularName, npmVersion);
        return localPath;
    }

}
