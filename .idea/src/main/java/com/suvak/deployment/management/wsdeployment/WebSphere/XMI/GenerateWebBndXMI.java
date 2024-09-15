package com.suvak.deployment.management.wsdeployment.WebSphere.XMI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GenerateWebBndXMI {
    public static void generateWebBndXMI(String path, String virtualHostName) throws IOException {
        String filePath = path + "/WebContent/WEB-INF/ibm-web-bnd.xmi";
        File file = new File(filePath);

        // Check if the file exists, and if it does, delete it
        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException("Failed to delete existing file: " + filePath);
            }
            System.out.println("Existing ibm-web-bnd.xmi file deleted.");
        }

        // Ensure the directory structure exists
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs(); // Create directories if they do not exist
        }

        // Write the new ibm-web-bnd.xmi file
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<webappbnd:WebAppBinding xmi:version=\"2.0\"\n");
            writer.write("    xmlns:xmi=\"http://www.omg.org/XMI\"\n");
            writer.write("    xmlns:webappbnd=\"webappbnd.xmi\"\n");
            writer.write("    xmi:id=\"WebAppBinding_1260469048387\"\n");
            writer.write("    virtualHostName=\"" + virtualHostName + "\">\n");
            writer.write("    <webapp href=\"WEB-INF/web.xml#WebApp_ID\" />\n");
            writer.write("</webappbnd:WebAppBinding>");
        }

        System.out.println("ibm-web-bnd.xmi generated at: " + filePath);
    }
}
