forfiles /P "C:\MYCAT\logs" /D -30 /C "cmd /c DEL /Q @FILE"
forfiles /P "C:\MYCAT\SAVE" /D -2 /C "cmd /c DEL /Q @FILE"
