#!/bin/bash

cd /home/olanto/MYCAT/dist

java -Xmx2000m -Djava.rmi.server.codebase="file:///home/olanto/MYCAT/dist/myCAT.jar" -Djava.security.policy="file:///home/olanto/MYCAT/rmi.policy"  -classpath "./myCAT.jar" org.olanto.mycat.main.StopAgent


