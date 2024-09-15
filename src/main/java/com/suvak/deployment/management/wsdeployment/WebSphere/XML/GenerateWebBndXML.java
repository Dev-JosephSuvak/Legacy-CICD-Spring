package com.suvak.deployment.management.wsdeployment.WebSphere.XML;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GenerateWebBndXML {
    public static void generateWebBndXml(String path, String virtualHost) throws IOException {
        String filePath = path + "/WebContent/WEB-INF/ibm-web-bnd.xml";
        File file = new File(filePath);

        // Check if the file exists, and if it does, delete it
        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException("Failed to delete existing file: " + filePath);
            }
            System.out.println("Existing ibm-web-bnd.xml file deleted.");
        }

        // Ensure the directory structure exists
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs(); // Create directories if not exist
        }

        // Write the new ibm-web-bnd.xml file
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<web-bnd\n");
            writer.write("    xmlns=\"http://websphere.ibm.com/xml/ns/javaee\"\n");
            writer.write("    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
            writer.write("    xsi:schemaLocation=\"http://websphere.ibm.com/xml/ns/javaee http://websphere.ibm.com/xml/ns/javaee/ibm-web-bnd_1_2.xsd\"\n");
            writer.write("    version=\"1.2\">\n\n");
            writer.write("    <virtual-host name=\"" + virtualHost + "\" />\n\n");
            writer.write("</web-bnd>");
        }

        System.out.println("ibm-web-bnd.xml generated at: " + filePath);
    }
}
