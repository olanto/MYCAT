C:
CD C:\MYCAT\run
net stop tomcat7

call C:\MYCAT\run\StopMYCATAgents.bat
call C:\MYCAT\run\conversion.bat
call C:\MYCAT\run\segmentation.bat
call C:\MYCAT\run\UpdateIndexAndMap.bat

call C:\MYCAT\run\ZipData.bat
call C:\MYCAT\run\cleanLogAndSave.bat


start  C:\MYCAT\run\StartMYCATAgents.bat