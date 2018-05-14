cd "C:\MYCAT"
net stop tomcat9
net start tomcat9
java -Dfile.encoding=UTF-8 -Xmx2000m -Djava.rmi.server.codebase="file:///C:/MYCAT/dist/myCAT.jar" -Djava.security.policy="file:///C:/MYCAT/rmi.policy"  -classpath "./dist/myCat.jar" org.olanto.mycat.main.RunAllServers2 
pause