C:
cd C:\MYCAT
rmdir data /s /q
mkdir data
xcopy /S C:\MYCAT\template\data C:\MYCAT\data

java -Dfile.encoding=UTF-8 -Xmx2000m -Djava.rmi.server.codebase="file:///C:/MYCAT/dist/myCAT.jar" -Djava.security.policy="file:///C:/MYCAT/rmi.policy"  -classpath "./dist/myCat.jar" org.olanto.mycat.main.CreateAllServers

pause