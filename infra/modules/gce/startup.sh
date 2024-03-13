#!/bin/bash

# Update package lists
sudo apt update

# Install Java 17
sudo apt install -y fontconfig openjdk-17-jre

# Install Jenkins
sudo wget -O /usr/share/keyrings/jenkins-keyring.asc \
  https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key
echo deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] \
  https://pkg.jenkins.io/debian-stable binary/ | sudo tee \
  /etc/apt/sources.list.d/jenkins.list > /dev/null
sudo apt-get update -y
sudo apt-get install -y jenkins

# Install Git
sudo apt-get install -y git

# Install kubectl
sudo apt-get install -y kubectl
