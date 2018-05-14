net stop tomcat7

call C:\MYCAT\run\StopMYCATAgents.bat

rem call "C:\MYCAT\run\delete conversion and segmentation.bat"
call "C:\MYCAT\run\delete index and map.bat"

rem call C:\MYCAT\run\conversion.bat
rem call C:\MYCAT\run\segmentation.bat
call C:\MYCAT\run\UpdateIndexAndMap.bat

rem start  C:\MYCAT\run\StartMYCATAgents.bat

pause

