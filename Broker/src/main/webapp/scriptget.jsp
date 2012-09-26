<%--
  ~ Copyright (c) 2012, SHIWA
  ~
  ~     This file is part of TrianaCloud.
  ~
  ~     TrianaCloud is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     (at your option) any later version.
  ~
  ~     TrianaCloud is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with TrianaCloud.  If not, see <http://www.gnu.org/licenses/>.
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
