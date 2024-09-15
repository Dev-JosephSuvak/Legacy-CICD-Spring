package com.suvak.deployment.management.wsdeployment.Angular.Interface;

import java.io.IOException;

public interface Angular {
    void compileAngularUI(String angularPath, String contextRoot, String angularAppNameinDist, String angularVersion, String serviceRepoPath) throws IOException;

}
