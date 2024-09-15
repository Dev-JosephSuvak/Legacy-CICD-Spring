# How to Deploy the Deployment API Application

## Introduction
This document provides instructions on how to deploy the Deployment API application. 
The Deployment API application is a Spring Boot application that allows users to deploy applications to WebSphere.
The application takes in a JSON request with the following parameters:
 `projectName`, `repoUrl`, `destinationFolder`, `virtualHost`, `username - [optional]`, `password - [optional]`

It is the user responsibility to ensure that the application is buildable and can be deployed successfully.

To avoid first time build issues:
1. If Legacy App, Ensure that the first time the application is deployed, all required JARs are listed in the Eclipse classpath.
2. If Maven App, Ensure that the first time the application is deployed, all required JARs are listed in the POM.xml file.
3. Verify the required application JARs for your project are in the `web-app/WEB-INF/lib` directory of the instance of 
the hosted deployment api you are deploying to, or are in the `WEB-INF/lib` directory of your project.
4. You only need to do this once for each project, or if the JARs are updated on the project.

## Key Considerations
1. The application is built using Java 17 and Spring Boot.
2. The application is built using Maven.
3. The SVN legacy deployment application should have a .classpath file that lists all the required JARs for the application to successfully build.
4. The Git deployment application should have a POM.xml file that lists all the required JARs for the application to successfully build.
5. For Angular applications, if you only build the Git/SVN endpoint, it is assumed the application has been built 
and the content of the have been copied to the WebContent folder before being committed to the repository. 
6. There are separate endpoints specifically for Angular SVN or Angular Git Repos. 


## Enhancements to be made
The current version of the Deployment API application was built with the following assumptions:
1. This application is only for deploying applications to WebSphere.
2. This application will be ONLY called by a Deployment Management UI
3. The legacy structure of the SVN repository is static and will not change there is no POM.xml file.
4. The Git repository structure is open to change with the POM.xml file or Eclipse classpath file.

Given more time, the following enhancements could be made:
1. Git should have multiple endpoints for either an eclipse classpath or a POM.xml file.
2. SVN should have multiple endpoints for either an eclipse classpath or a POM.xml file.
3. Deployment of UI applications to saved repositories.


## Table of Contents
The document contains the following Information:
1. [Pre-requisites](#Pre-requisites)
2. [Steps](#Steps)
3. [Adjustments Made](#Adjustments-Made)
4. [Happy Path Flow](#Happy-Path-Flow)
5. [Source Controller](#SourceController)
6. [RepoService](#RepoService)
7. [WebSphere/Interface/WebSphere](#WebSphere/Interface/WebSphere)
8. [WebSphere/Implementation/WebSphereImpl](#WebSphere/Implementation/WebSphereImpl)
9. [GenerateWebBndXMI](#WebSphere/XMI/GenerateWebBndXMI)
10. [GenerateWebExtXMI](#WebSphere/XMI/GenerateWebExtXMI)

## Pre-requisites
1. Java 17 or higher with WAS 9 (configured for Java 8)
2. Maven
3. Git
4. SVN
5. SVN Client w/ Full Command Line- TortoiseSVN located at: C:\Program Files\TortoiseSVN\bin
6. Git w/ a token for authentication.
7. Creation of a C:\deployments\deploymentFiles directory (for the application to deploy to).
8. Each Project (if being deployed via this tool for the *_first time_* should have all required JARs listed in the Eclipse classpath).
  a. This is to ensure that the application can be built and deployed successfully.

## Steps
1. Clone the repository to your local machine.
2. Open the terminal and navigate to the project directory.
3. Run the following command to build the project:
    ```shell
    mvn clean install
    ```
4. Run the following command to start the application:
    ```shell
    mvn spring-boot:run
    ```
5. The application will be accessible at a default port or via `http://localhost:8080`.
6. Request needs to be made like:
This is the Git or SVN endpoints.
```JSON
{
    "projectName": "repoName",
    "repoUrl": "SubDirectory to SVN or Git Host",
    "destinationFolder": "C:/deployments/deploymentFiles",
    "virtualHost": "intranet_host/extranet_host/etc"
}
```
OR like: This is the Angular Repo
```JSON
{
    "projectName": "This is the contextRoot from WebSphere",
    "angularProjectName": "This is the technical project name; the base root name",
    "angularVersion": "This is the version of Node you use to build the Angular Repo",
    "angularRepoUrl": "Here is the Repo for Angular",
    "serviceRepoUrl": "Here is the Repo for the Service",
    "destinationFolder": "C:/deployments/deploymentFiles",
    "virtualHost": "intranet_host/extranet_host/etc"
}
```

## SVN 
## Adjustments Made

Ensure a local SVN client exists such as TortoiseSVN or SlikSVN.

1. C:\Users\<Workstation>\AppData\Roaming\Subversion\servers
2. Ensure the following is added to the servers file
   ```shell
   [global]
   http-auth-types = basic;digest;negotiate;ntlm
   ssl-trust-default-ca = yes

3. Command Prompt should download an example directory from SVN. 

### Happy Path Flow

1. [Source Controller](#SourceController) - Intakes the JSON body and determines the source control type via the URL.
2. [RepoService](#RepoService) - Determines the source control type and calls the appropriate service.
3. [RepoService](#RepoService) - Download the Repo and Package the WebSphere files (passes on request info to WS Interface).
- [RepoService](#RepoService) - checkoutSvnRepo will use SVN to download the repo & package the WebSphere required files into the repo.
- [RepoService](#RepoService) - cloneGitRepo will use Git to download the repo & package the WebSphere required files into the repo.
4. [WebSphere](#WebSphere/Interface/WebSphere) - Interface to WebSphere to deploy the application.
- [WebSphere](#WebSphere/Implementation/WebSphereImpl) - GenerateXMIFiles will generate the XMI binding files for WebSphere using the methods in Impl.
- [WebSphere](#WebSphere/Implementation/WebSphereImpl) - GenerateXMLFiles will generate the XML binding files for WebSphere using the methods in Impl.
5. [GenerateWebBndXMI](#WebSphere/XMI/GenerateWebBndXMI) - This will use generated ibm-web-bnd.xmi file to deploy the application to WebSphere.
6. [GenerateWebExtXMI](#WebSphere/XMI/GenerateWebExtXMI) - This will use the generated ibm-web-ext.xmi file to deploy the application to WebSphere.
7. [GenerateWebBndXML](#WebSphere/XML/GenerateWebBndXML) - This will use the generated ibm-web-bnd.xml file to deploy the application to WebSphere.
8. [GenerateWebExtXML](#WebSphere/XML/GenerateWebExtXML) - This will use the generated ibm-web-ext.xml file to deploy the application to WebSphere.
9. [WebSphere](#WebSphere/Interface/WebSphere) - DeployApplication will Execute JAR Generation.
10. [WebSphere](#WebSphere/Packaging/GenerateRequiredJARs) - This will execute the reading of the classpath files, and copy necessary JARs to WEB-INF/lib from eclipse classpath or POM.xml.
11. [RepoService](#RepoService) - After all files are generated, the RepoService will drop the files at the specified destination & return the URL.
12. [Source Controller](#SourceController) - The Source Controller will call the PackagingAndMoveFiles method to package the WAR and EAR files from the directory.
13. [Source Controller](#SourceController) - The WAR file is packaged using the RepoService.packageWar method.
14. [RepoService](#RepoService) - The WAR file is packaged using ANT packaging method.
15. [Source Controller](#SourceController) - The EAR file is packaged using the RepoService.packageEar method.
15. [RepoService](#RepoService) - The EAR file is packaged using ANT packaging method.
16. [Source Controller](#SourceController) - The Source Controller endpoint instructions will move the files to the destination folder.
17. [RepoService](#RepoService) - If the destination folder has a file already, the old file will have a timestamp appended to the end of the file name.
18. [Source Controller](#SourceController) - The Source Controller will return a success message to user.

## Git
## Adjustments Made

Ensure Git command client exists on the server. 

### Happy Flow
1. [Source Controller](#SourceController) - Intakes the JSON body and determines the source control type via the URL.
2. [RepoService](#RepoService) - Determines the source control type and calls the appropriate service.
3. [RepoService](#RepoService) - Download the Repo and Package the WebSphere files (passes on request info to WS Interface).
   - [RepoService](#RepoService) - checkoutSvnRepo will use SVN to download the repo & package the WebSphere required files into the repo.
   - [RepoService](#RepoService) - cloneGitRepo will use Git to download the repo & package the WebSphere required files into the repo.
4. [WebSphere](#WebSphere/Interface/WebSphere) - Interface to WebSphere to deploy the application.
   - [WebSphere](#WebSphere/Implementation/WebSphereImpl) - GenerateXMIFiles will generate the XMI binding files for WebSphere using the methods in Impl.
   - [WebSphere](#WebSphere/Implementation/WebSphereImpl) - GenerateXMLFiles will generate the XML binding files for WebSphere using the methods in Impl.
5. [GenerateWebBndXMI](#WebSphere/XMI/GenerateWebBndXMI) - This will use generated ibm-web-bnd.xmi file to deploy the application to WebSphere.
6. [GenerateWebExtXMI](#WebSphere/XMI/GenerateWebExtXMI) - This will use the generated ibm-web-ext.xmi file to deploy the application to WebSphere.
7. [GenerateWebBndXML](#WebSphere/XML/GenerateWebBndXML) - This will use the generated ibm-web-bnd.xml file to deploy the application to WebSphere.
8. [GenerateWebExtXML](#WebSphere/XML/GenerateWebExtXML) - This will use the generated ibm-web-ext.xml file to deploy the application to WebSphere.
9. [WebSphere](#WebSphere/Interface/WebSphere) - DeployApplication will Execute JAR Generation.
10. [WebSphere](#WebSphere/Packaging/GenerateRequiredJARs) - This will execute the reading of the classpath files, and copy necessary JARs to WEB-INF/lib from eclipse classpath or POM.xml.
11. [RepoService](#RepoService) - After all files are generated, the RepoService will drop the files at the specified destination & return the URL.
12. [Source Controller](#SourceController) - The Source Controller will call the PackagingAndMoveFiles method to package the WAR and EAR files from the directory.
13. [Source Controller](#SourceController) - The WAR file is packaged using the RepoService.packageWar method.
14. [RepoService](#RepoService) - The WAR file is packaged using ANT packaging method.
15. [Source Controller](#SourceController) - The EAR file is packaged using the RepoService.packageEar method.
15. [RepoService](#RepoService) - The EAR file is packaged using ANT packaging method.
16. [Source Controller](#SourceController) - The Source Controller endpoint instructions will move the files to the destination folder.
17. [RepoService](#RepoService) - If the destination folder has a file already, the old file will have a timestamp appended to the end of the file name.
18. [Source Controller](#SourceController) - The Source Controller will return a success message to user.

## Angular
## Adjustments Made

No extra adjustments at this time. 

### Happy Flow
1. [Source Controller](#SourceController) - Intakes the JSON body and determines the source control type via the URL.
2. [RepoService](#RepoService) - Determines the source control type and calls the appropriate service.
3. [RepoService](#RepoService) - Download the Repo and Package the WebSphere files (passes on request info to WS Interface).
   - [RepoService](#RepoService) - checkoutSvnRepo will use SVN to download the repo & package the WebSphere required files into the repo.
   - [RepoService](#RepoService) - cloneGitRepo will use Git to download the repo & package the WebSphere required files into the repo.
4. [WebSphere](#WebSphere/Interface/WebSphere) - Interface to WebSphere to deploy the application.
   - [WebSphere](#WebSphere/Implementation/WebSphereImpl) - GenerateXMIFiles will generate the XMI binding files for WebSphere using the methods in Impl.
   - [WebSphere](#WebSphere/Implementation/WebSphereImpl) - GenerateXMLFiles will generate the XML binding files for WebSphere using the methods in Impl.
5. [GenerateWebBndXMI](#WebSphere/XMI/GenerateWebBndXMI) - This will use generated ibm-web-bnd.xmi file to deploy the application to WebSphere.
6. [GenerateWebExtXMI](#WebSphere/XMI/GenerateWebExtXMI) - This will use the generated ibm-web-ext.xmi file to deploy the application to WebSphere.
7. [GenerateWebBndXML](#WebSphere/XML/GenerateWebBndXML) - This will use the generated ibm-web-bnd.xml file to deploy the application to WebSphere.
8. [GenerateWebExtXML](#WebSphere/XML/GenerateWebExtXML) - This will use the generated ibm-web-ext.xml file to deploy the application to WebSphere.
9. [WebSphere](#WebSphere/Interface/WebSphere) - DeployApplication will Execute JAR Generation.
10. [WebSphere](#WebSphere/Packaging/GenerateRequiredJARs) - This will execute the reading of the classpath files, and copy necessary JARs to WEB-INF/lib from eclipse classpath or POM.xml.
11. [RepoService](#RepoService) - All WebSphere files are generated.
12. [Angular](#Angular/Impl/GenerateRequiredJARs) - Angular code gets pulled from SVN or Git.
13. [Angular](#Angular/Packaging/dist/CompileDist) - Angular code compiled via NVM/NPM commands.
14. [Angular](#Angular/Packaging/dist/CompileDist) - Angular code in dist folder when compiled.
15. [Angular](#Angular/Packaging/dist/UnpackDist)) - Angular code unpacks dist folder and puts it in the active service folder's WEB-INF/Web-Content Folder. 
16. [RepoService](#RepoService) - The RepoService will drop the files at the specified destination & return the URL.
16. [Source Controller](#SourceController) - The Source Controller will call the PackagingAndMoveFiles method to package the WAR and EAR files from the directory. 
17. [Source Controller](#SourceController) - The WAR file is packaged using the RepoService.packageWar method. 
18. [RepoService](#RepoService) - The WAR file is packaged using ANT packaging method.
19. [Source Controller](#SourceController) - The EAR file is packaged using the RepoService.packageEar method.
20. [RepoService](#RepoService) - The EAR file is packaged using ANT packaging method. 
21. [Source Controller](#SourceController) - The Source Controller endpoint instructions will move the files to the destination folder. 
22. [RepoService](#RepoService) - If the destination folder has a file already, the old file will have a timestamp appended to the end of the file name. 
23. [Source Controller](#SourceController) - The Source Controller will return a success message to user.

## Sample JSON requests

### Endpoints

#### svn
```JSON
{
  "projectName": "servrepo",
  "repoUrl": "repo/Service",
  "destinationFolder": "C:/deployments/deploymentFiles",
  "virtualHost": "default_host"
}
```

#### git
```JSON
{
    "projectName": "servrepo",
    "repoUrl": "Service.git",
    "destinationFolder": "C:/deployments/deploymentFiles",
    "virtualHost": "default_host"
}
```

#### angular/svn
```JSON
{
"projectName": "angrepo",
"angularProjectName": "ANG-REPO",
"angularVersion": "16.10.2",
"angularRepoUrl": "repo/branches/devbranch/Angular",
"serviceRepoUrl": "repo/Service",
"destinationFolder":  "C:/deployments/deploymentFiles",
"virtualHost": "default_host"
}
```


#### angular/git
```JSON
{
"projectName": "angrepo",
"angularProjectName": "ANG-REPO",
"angularVersion": "16.10.2",
"angularRepoUrl": "Angular.git",
"serviceRepoUrl": "Service.git",
"destinationFolder":  "C:/deployments/deploymentFiles",
"virtualHost": "default_host"
}
```


