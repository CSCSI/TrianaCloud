<%--
  ~ Licensed to the Apache Software Foundation (ASF) under one
  ~        or more contributor license agreements.  See the NOTICE file
  ~        distributed with this work for additional information
  ~        regarding copyright ownership.  The ASF licenses this file
  ~        to you under the Apache License, Version 2.0 (the
  ~        "License"); you may not use this file except in compliance
  ~        with the License.  You may obtain a copy of the License at
  ~
  ~          http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~        Unless required by applicable law or agreed to in writing,
  ~        software distributed under the License is distributed on an
  ~        "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  ~        KIND, either express or implied.  See the License for the
  ~        specific language governing permissions and limitations
  ~        under the License.
  --%>

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
