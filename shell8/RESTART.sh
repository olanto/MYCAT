#!/bin/bash

sudo /etc/init.d/tomcat8 stop

/home/olanto/MYCAT/shell/StopMYCATAgents.sh

/bin/bash /home/olanto/MYCAT/shell/StartMYCATAgents.sh

