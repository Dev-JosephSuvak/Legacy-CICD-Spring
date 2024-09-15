package com.suvak.deployment.management.wsdeployment.WebSphere.Interface;

public interface WebSphere {
        // Method to generate XML files
        void generateXmlFiles(String path, String contextRoot, String virtualHost);

        // Method to generate XMI files
        void generateXmiFiles(String path, String contextRoot, String virtualHost);

        void generateJARFiles(String repoClassFilePath, String repoWebINFPath);
}
