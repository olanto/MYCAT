#!/bin/bash


sudo /etc/init.d/tomcat7 stop

/home/olanto/MYCAT/shell/StopMYCATAgents.sh

'/home/olanto/MYCAT/shell/delete conversion and segmentation.sh'
'/home/olanto/MYCAT/shell/delete index and map.sh'


/home/olanto/MYCAT/shell/conversion.sh
/home/olanto/MYCAT/shell/segmentation.sh
/home/olanto/MYCAT/shell/UpdateIndexAndMap.sh

/bin/bash /home/olanto/MYCAT/shell/StartMYCATAgents.sh


