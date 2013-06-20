net stop tomcat7

call C:\MYCAT\run\StopMYCATAgents.bat

call "C:\MYCAT\run\delete conversion and segmentation.bat"
call "C:\MYCAT\run\delete index and map.bat"

call C:\MYCAT\run\conversion.bat
call C:\MYCAT\run\segmentation.bat
call C:\MYCAT\run\UpdateIndexAndMap.bat

start  C:\MYCAT\run\StartMYCATAgents.bat

