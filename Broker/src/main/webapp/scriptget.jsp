#!/bin/bash
#at some stage, this will be some clever code to do codey things with code

echo "10.0.0.4 i9.cscloud.cf.ac.uk" >> /etc/hosts

wget http://i9.cscloud.cf.ac.uk/webbed-nfs/nfs/zips/home.tar.gz -O /tmp/home.tar.gz

mv /home/ubuntu/.ssh /tmp

cd /
tar xvf /tmp/home.tar.gz

chown ubuntu:ubuntu -R /home/ubuntu/

if [ -e /dev/vdb ]
then
        mkfs.ext4 /dev/vdb
        mkdir /tmp/mnt
        mount /dev/vdb /tmp/mnt
        mv /home/ubuntu /tmp/mnt
        ln -s /tmp/mnt/ubuntu /home/ubuntu
fi

cd /home/ubuntu
mv /tmp/.ssh /home/ubuntu/.ssh
su ubuntu -c 'screen -dm java -jar Worker-1.0-jar-with-dependencies.jar'
