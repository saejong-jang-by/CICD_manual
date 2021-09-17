# CI/CD pipeline manual

- This project contains basic structures of backend logic with AWS Lambda and CI/CD pipeline with Docker, Jenkins, and with other AWS services.

## Steps

### Appspec.yml

- project needs appspec.yml for deployment 
  This will run Shell scripts depending on the life cycle
- name has to be appspec.yml
- appspec.yml has to be located at the root of the project
    
### Create repo and push the project

- create a git repository and push maven or Gradle project 

### EC2 with Linux and ssh

- set up EC2 with Linux machine 
- ssh to EC2 instance from terminal 
- ssh -i ~/.ssh/**{FILE_NAME}**.pem ec2-user@ec2-**{EC2_PUBLIC_IP}**.us-west-2.compute.amazonaws.com

### Jenkins on Docker 

install Docker on EC2 instance and run

- sudo yum install -y java-1.8.0-openjdk-devel.x86_64
- sudo amazon-linux-extras install -y docker
- sudo service docker start
- docker run -d --name **{CONTAINER_NAME}** -p 8080:8080 **{IMAGE}** (If there is no container to run) 
    
   ex. docker run -d --name jenkins -p 8080:8080 jenkins/jenkins:jdk11
- (run existing container) docker start **{CONTAINER_NAME}**
   
   ex. docker start Jenkins
    
- sudo su - (always use docker in root user)

- **[Docker Commands](https://docs.docker.com/engine/reference/commandline/cli/)**

### Port in security Inbound

- Once everything is set up, go to EC2 instance and click security groups
- Add two inbounds with TCP port 8080 (0.0.0.0/0, ::/0)
- grab public IP from EC2 instance and access in the browser with port 8080 
  
  ex. http://{PUBLIC_IP}:8080

### Initial setups for Jenkins

- docker exec -it Jenkins bash (Jenkins container in bash)
- cat /var/jenkins_home/secrets/initialAdminPassword (getting admin password for initial login)
- click suggested plugin install
- click manage Jenkins and click the available tab
- download **AWS CodeDeploy Plugin for Jenkins, Amazon Web Service SDK**
 
### EC2 instances with different subnet

**Create two EC2 instances with a different subnet and apply for each role**
- create IAM roles with policies: "AWSCodeDeplooyFullAccess, AmazonS3FullAccess" 
- go to step 3 change config and select subnet (a)

- Add the script in user data
  
  ex. 
  sudo rpm --import https://yum.corretto.aws/corretto.key
  
  sudo curl -L -o /etc/yum.repos.d/corretto.repo https://yum.corretto.aws/corretto.repo
  
  sudo yum install -y java-11-amazon-corretto-devel
  
  sudo yum -y update
  
  sudo yum install -y ruby
  
  cd /home/ec2-user
  
  curl -O https://aws-codedeploy-us-west-2.s3.us-west-2.amazonaws.com/latest/install
  
  sudo chmod +x ./install
  
  sudo ./install auto

- add a tag with any name
- add two HTTP with TCP allowing port 80, source 0.0.0.0/0 and ::/0
- repeat the process with a different subnet (c)

### Load Balancers

- create LoadBalancers with HTTP/HTTPS
- mapping it with instances from the last step
- register target group with the two instances
- add target group 

### CodeDeploy

- create CodeDeploy development configuration 
- type name, select EC2/On-Promises, select Number, put the value as 1

**This will tell if the deployment is a success or failure. We need at least one instance is available during deployment**

- Go to the Applications tab and create a CodeDeploy application
- Create deployment group
  Create IAM role with codedeploy-role and get ARN
  And change trust relationship
    change value for "Service" as "codedeploy.amazonaws.com" 

- After creating a new IAM role, create cicd-group

### Jenkins Project

**Jenkins project will download the code. It will test and build when the code is pushed and request deployment to CodeDeploy**

- New item -> freestyle project
- Source Code Management -> Git -> put git URL -> set up branch -> create credential with GitHub ID and password
- Select GitHub hook trigger for GITScm polling
- Add build step -> select Maven version

- Add Execute shell

  rm -f *.jar (delete the files that exist, it will prevent file exist exception)
  
- Add another Excuse shell
  
  cd build/libs/
  mv *.jar ~/workspace/cicd-jenkins-project/
  (It will move files that created by bootJar)
  
**bundling files, upload it to S3 and set up CodeDeploy pipeline**

- post-build action -> Deploy an application to AWS CodeDeploy
- Add details from CodeDeployment Group
- Include files will select the files that we want to bundle from $JENKINS_HOME/workspace
- Add IAM user with two policies (AWSCoodeDeployFullAccess, AmazonS3FullAccess)
- And put access key and secret keys from IAM user 

### WebHook

- Add domain:port/github-webhook/
- And change contentType to Application/json
- test commit
