
call  C:\MYCAT\run\SetFileName.bat

C:
cd "C:\MYCAT\"
java -Dfile.encoding=UTF-8 -Xmx200m -Djava.rmi.server.codebase="file:///C:/MYCAT/dist/myCAT.jar" -Djava.security.policy="file:///C:/MYCAT/rmi.policy"  -classpath "./dist/myCat.jar" org.olanto.mycat.main.StopAgent > "C:\MYCAT\logs\v-cats_stop_logs-%filename%.txt"
