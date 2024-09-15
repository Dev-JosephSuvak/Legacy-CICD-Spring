package com.suvak.deployment.management.wsdeployment.WebSphere.Packaging;

import com.suvak.deployment.management.wsdeployment.WebSphere.XML.GenerateWebBndXML;
import com.suvak.deployment.management.wsdeployment.WebSphere.XML.GenerateWebExtXML;

import java.io.IOException;

public class GenerateXMLFiles  {
    public static void generateXmlFiles(String path, String contextRoot, String virtualHost) {
        try {
            GenerateWebExtXML.generateWebExtXml(path, contextRoot);
            GenerateWebBndXML.generateWebBndXml(path, virtualHost);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
