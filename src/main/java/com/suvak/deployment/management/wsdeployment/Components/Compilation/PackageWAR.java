package com.suvak.deployment.management.wsdeployment.Components.Compilation;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Jar;

import java.io.File;

public class PackageWAR {
    public void packageWar(String sourceDir, String outputWar) {
        Project project = new Project();
        project.init();

        Jar jarTask = new Jar();
        jarTask.setProject(project);
        jarTask.setDestFile(new File(outputWar));
        jarTask.setBasedir(new File(sourceDir));

        // Exclude the output WAR file from being included in the ZIP
        jarTask.setExcludes(new File(outputWar).getName());

        jarTask.setIncludes("**/*"); // Include everything else in the folder

        jarTask.execute();
        System.out.println("WAR file created at: " + new File(outputWar).getAbsolutePath());
    }

}
