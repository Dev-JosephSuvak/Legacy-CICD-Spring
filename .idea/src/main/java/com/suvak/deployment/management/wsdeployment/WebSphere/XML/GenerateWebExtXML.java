package com.suvak.deployment.management.wsdeployment.WebSphere.XML;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GenerateWebExtXML {
    public static void generateWebExtXml(String path, String contextRoot) throws IOException {
        String filePath = path + "/WebContent/WEB-INF/ibm-web-ext.xml";
        File file = new File(filePath);

        // Check if the file exists, and if it does, delete it
        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException("Failed to delete existing file: " + filePath);
            }
            System.out.println("Existing ibm-web-ext.xml file deleted.");
        }

        // Ensure the directory structure exists
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs(); // Create directories if they do not exist
        }

        // Write the new ibm-web-ext.xml file
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<web-ext\n");
            writer.write("    xmlns=\"http://websphere.ibm.com/xml/ns/javaee\"\n");
            writer.write("    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
            writer.write("    xsi:schemaLocation=\"http://websphere.ibm.com/xml/ns/javaee http://websphere.ibm.com/xml/ns/javaee/ibm-web-ext_1_1.xsd\"\n");
            writer.write("    version=\"1.1\">\n\n");
            writer.write("    <reload-interval value=\"3\"/>\n");
            writer.write("    <context-root uri=\"" + contextRoot + "\" />\n");
            writer.write("    <enable-directory-browsing value=\"false\"/>\n");
            writer.write("    <enable-file-serving value=\"true\"/>\n");
            writer.write("    <enable-reloading value=\"true\"/>\n");
            writer.write("    <enable-serving-servlets-by-class-name value=\"false\" />\n\n");
            writer.write("</web-ext>");
        }

        System.out.println("ibm-web-ext.xml generated at: " + filePath);
    }
}
