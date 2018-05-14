C:
cd C:\MYCAT\template
rmdir data_empty /s /q
mkdir data_empty 
xcopy /S C:\MYCAT\data C:\MYCAT\template\data_empty

pause