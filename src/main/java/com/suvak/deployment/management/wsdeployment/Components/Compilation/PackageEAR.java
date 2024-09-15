package com.suvak.deployment.management.wsdeployment.Components.Compilation;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

import java.io.File;

public class PackageEAR {
    public void packageEar(String warFilePath, String outputEar) {
        Project project = new Project();
        project.init();

        Zip earTask = new Zip();
        earTask.setProject(project);
        earTask.setDestFile(new File(outputEar));

        FileSet fileSet = new FileSet();
        fileSet.setDir(new File(warFilePath).getParentFile());
        fileSet.setIncludes(new File(warFilePath).getName()); // Include only the WAR file

        // Exclude the output EAR file from being included
        earTask.setExcludes(new File(outputEar).getName());

        earTask.addFileset(fileSet);

        earTask.execute();
        System.out.println("EAR file created at: " + new File(outputEar).getAbsolutePath());
    }
}
