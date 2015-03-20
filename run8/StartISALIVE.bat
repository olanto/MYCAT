call  C:\MYCAT\run\SetFileName.bat

cd "C:\MYCAT"
java -Dfile.encoding=UTF-8 -Xmx2000m -Djava.rmi.server.useCodebaseOnly=false -Djava.rmi.server.codebase="file:///C:/MYCAT/dist/MoreTools.jar" -Djava.security.policy="file:///C:/MYCAT/rmi.policy"  -classpath "./dist/MoreTools.jar" moretools.IsAlive > "C:\MYCAT\logs\is_alive-%filename%.txt"
exit