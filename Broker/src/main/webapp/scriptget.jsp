#!/bin/bash
#at some stage, this will be some clever code to do codey things with code

echo "10.0.0.4 i9.cscloud.cf.ac.uk" >> /etc/hosts

wget http://i9.cscloud.cf.ac.uk/webbed-nfs/nfs/zips/home.tar.gz -O /tmp/home.tar.gz

cd /
tar xvf /tmp/home.tar.gz
cd /home/ubuntu
rm triana-app-4.0.0-SNAPSHOT.jar
wget http://i9.cscloud.cf.ac.uk/webbed-nfs/nfs/zips/triana-app-4.0.0-SNAPSHOT.jar
chown ubuntu:ubuntu triana-app-4.0.0-SNAPSHOT.jar

chown ubuntu /home/ubuntu/.triana4/org.trianacode.properties
su ubuntu -c 'screen -dm java -jar Worker-1.0-jar-with-dependencies.jar'
