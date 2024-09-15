package com.suvak.deployment.management.wsdeployment.Angular.Implementation;


import com.suvak.deployment.management.wsdeployment.Angular.Interface.Angular;
import com.suvak.deployment.management.wsdeployment.Angular.Packaging.dist.CompileDist;
import com.suvak.deployment.management.wsdeployment.Angular.Packaging.unpackDist.UnpackDist;

import java.io.IOException;

public class AngularImpl implements Angular {
    @Override
    public void compileAngularUI(String angularPath, String contextRoot, String angularAppNameinDist, String angularVersion, String serviceRepoPath) throws IOException {
        CompileDist.generateDist(angularPath, contextRoot, angularVersion);
        UnpackDist.unpackDist(angularPath + "/dist", angularAppNameinDist, serviceRepoPath);
    }
}
