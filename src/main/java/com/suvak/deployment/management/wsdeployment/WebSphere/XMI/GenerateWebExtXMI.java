package com.suvak.deployment.management.wsdeployment.WebSphere.XMI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GenerateWebExtXMI {
    public static void generateWebExtXMI(String path) throws IOException {
        String filePath = path + "/WebContent/WEB-INF/ibm-web-ext.xmi";
        File file = new File(filePath);

        // Check if the file exists, and if it does, delete it
        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException("Failed to delete existing file: " + filePath);
            }
            System.out.println("Existing ibm-web-ext.xmi file deleted.");
        }

        // Ensure the directory structure exists
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs(); // Create directories if they do not exist
        }

        // Write the new ibm-web-ext.xmi file
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<webappext:WebAppExtension xmi:version=\"2.0\"\n");
            writer.write("    xmlns:xmi=\"http://www.omg.org/XMI\"\n");
            writer.write("    xmlns:webappext=\"webappext.xmi\"\n");
            writer.write("    xmi:id=\"WebAppExtension_1260469048387\"\n");
            writer.write("    reloadInterval=\"3\"\n");
            writer.write("    reloadingEnabled=\"true\"\n");
            writer.write("    additionalClassPath=\"\"\n");
            writer.write("    fileServingEnabled=\"true\"\n");
            writer.write("    directoryBrowsingEnabled=\"false\"\n");
            writer.write("    serveServletsByClassnameEnabled=\"true\">\n");
            writer.write("    <webApp href=\"WEB-INF/web.xml#WebApp_ID\" />\n");
            writer.write("    <jspAttributes xmi:id=\"JSPAttribute_123456789\" name=\"jdkSourceLevel\" value=\"15\" />\n");
            writer.write("</webappext:WebAppExtension>");
        }

        System.out.println("ibm-web-ext.xmi generated at: " + filePath);
    }
}
