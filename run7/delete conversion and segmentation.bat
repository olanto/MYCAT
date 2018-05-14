C:
cd C:\MYCAT\corpus
rmdir txt /s /q
mkdir txt
xcopy /S C:\MYCAT\template\txt C:\MYCAT\corpus\txt
#rmdir source /s /q
#mkdir source
#rmdir bad /s /q
#mkdir bad
cd C:\MYCAT
rmdir TEMP /s /q
mkdir TEMP
rmdir logs /s /q
mkdir logs