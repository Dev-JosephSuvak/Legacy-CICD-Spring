package com.suvak.deployment.management.wsdeployment.Entities;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AngularRequest {
    private String projectName;
    private String angularProjectName;
    private String angularVersion;
    private String angularRepoUrl;
    private String serviceRepoUrl;
    private String destinationFolder;
    private String virtualHost;
}
