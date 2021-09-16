#!/bin/bash
find /opt/codedeploy-agent/deployment-root/deployment/* -maxdepth 0 -type 'd' | grep -v
$(stat -c '%Y:%n'  /opt/codedeploy-agent/deployment-root/deployment/* | sort -t: -n | tail  -l | cut -d: -f2- | cut -c 3-) | xargs rm -rf
sudo rm -rf /home/ec2-user/*