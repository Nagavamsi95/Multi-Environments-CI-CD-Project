# Multi-Environment CI/CD Pipeline for a Java Web App

![Pipeline](https://github.com/Nagavamsi95/Multi-Environments-CI-CD-Project/actions/workflows/main.yml/badge.svg)

A Maven Java web application built once with **GitHub Actions** and deployed, in order, to four **Apache Tomcat** servers on **AWS EC2**: **DEV, TEST, PREPROD and PROD**. Production waits for a manual approval.

## Architecture

```
Developer push to main
        |
GitHub Actions: build job (JDK 17, mvn clean package, upload WAR)
        |
deploy-dev -> deploy-test -> deploy-preprod -> [manual approval] -> deploy-prod
    |             |               |                                    |
Tomcat DEV   Tomcat TEST     Tomcat PREPROD                       Tomcat PROD
```

## Tech stack

| Area | Tool |
|---|---|
| CI/CD | GitHub Actions (environments, secrets, required reviewers) |
| Build | Maven, Temurin JDK 17 |
| Runtime | Apache Tomcat 9 on Ubuntu |
| Cloud | AWS EC2, security groups, AMI |
| Language | Java (Servlet + JSP) |

## How it works

1. A push to `main` starts the workflow in `.github/workflows/main.yml`.
2. The **build** job compiles the app and uploads one `java-my-webapp.war` as an artifact.
3. Each **deploy** job downloads that same WAR and uploads it to its Tomcat server through the Tomcat Manager API (`/manager/text/deploy`).
4. Every stage needs the previous one to succeed, so a failure in DEV, TEST or PREPROD stops the pipeline before PROD.
5. The **PROD** environment has a required reviewer, so the final deployment waits for approval.
6. Each deploy step checks that Tomcat answers `OK`, so a refused deployment fails the job instead of passing silently.

## Project structure

```
.
|-- .github/workflows/main.yml     # the pipeline
|-- pom.xml                        # Maven build, WAR packaging
`-- src/main
    |-- java/com/nagavamsi/app/HelloServlet.java
    `-- webapp/index.jsp
```

## Setup summary

1. Create a security group allowing TCP **8080** and SSH **22**.
2. Launch an Ubuntu EC2 server, install Java and Tomcat, enable the Manager user, and start Tomcat as a systemd service.
3. Create an AMI from it and launch three more servers (TEST, PREPROD, PROD).
4. Create four GitHub environments: `DEV`, `TEST`, `PREPROD`, `PROD`.
5. Add three secrets to each environment, using the environment name as prefix:
   - `<ENV>_TOMCAT_USERNAME`
   - `<ENV>_TOMCAT_PASSWORD`
   - `<ENV>_TOMCAT_URL` (for example `http://<server-ip>:8080`)
6. In **PROD**, enable **Required reviewers**.
7. Push to `main` and approve the PROD deployment in the Actions tab.

## Verify

Open each server:

```
http://<server-ip>:8080/java-my-webapp/hello
```

It shows a "Deployed Successfully!" page with the server host name and time. On the server, `ls /opt/tomcat/webapps` shows `java-my-webapp.war`.

## Possible improvements

- Replace the shared Tomcat login with strong credentials, or deploy over SSH
- Restrict ports 8080 and 22 to known IP addresses, or use a load balancer
- Add automated tests and a health check after each deployment
- Add rollback, and create the servers with Terraform

## Author

Naga Vamsi, aspiring DevOps engineer. GitHub: [Nagavamsi95](https://github.com/Nagavamsi95)
