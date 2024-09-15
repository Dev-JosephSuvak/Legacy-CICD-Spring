package com.suvak.deployment.management.wsdeployment.WebSphere.Implementation;

import com.suvak.deployment.management.wsdeployment.WebSphere.Interface.WebSphere;
import com.suvak.deployment.management.wsdeployment.WebSphere.Packaging.GenerateRequiredJars;
import com.suvak.deployment.management.wsdeployment.WebSphere.Packaging.GenerateXMIFiles;
import com.suvak.deployment.management.wsdeployment.WebSphere.Packaging.GenerateXMLFiles;
import org.springframework.beans.factory.annotation.Value;

public class WebSphereImpl implements WebSphere {

        @Value("infra.lib.jarpath")
        String deploymentAppJarPath;
        @Override
        public void generateXmlFiles(String path, String contextRoot, String virtualHost) {
            // Call the method from GenerateXMLFiles class
            GenerateXMLFiles.generateXmlFiles(path, contextRoot, virtualHost);
        }

        @Override
        public void generateXmiFiles(String path, String contextRoot, String virtualHost) {
            // Call the method from GenerateXMIFiles class
            GenerateXMIFiles.generateXmiFiles(path, contextRoot, virtualHost);
        }

        @Override
        public void generateJARFiles(String repoClasspathFilePath, String repoWebInfLibPath) {
            // Create an instance of GenerateRequiredJars
            GenerateRequiredJars jarGenerator = new GenerateRequiredJars(repoClasspathFilePath, repoWebInfLibPath, deploymentAppJarPath);
            jarGenerator.processJars();
        }

}
