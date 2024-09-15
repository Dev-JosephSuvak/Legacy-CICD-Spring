package com.suvak.deployment.management.wsdeployment.WebSphere.Packaging;

import com.suvak.deployment.management.wsdeployment.WebSphere.XMI.GenerateWebBndXMI;
import com.suvak.deployment.management.wsdeployment.WebSphere.XMI.GenerateWebExtXMI;

import java.io.IOException;

public class GenerateXMIFiles {

    public static void generateXmiFiles(String path, String contextRoot, String virtualHost) {
        try {
            GenerateWebBndXMI.generateWebBndXMI(path, virtualHost);
            GenerateWebExtXMI.generateWebExtXMI(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
