package com.suvak.deployment.management.wsdeployment.Entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RepoRequest {
    private String projectName;
    private String repoUrl;
    private String destinationFolder;
    private String virtualHost;
    private String username;
    private String password;
}
