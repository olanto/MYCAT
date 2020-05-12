call  C:\MYCAT\run\SetFileName.bat

java -Xmx4048m -jar "C:\MYCAT\dist\SimpleConverter.jar" "C:\MYCAT\config\CONV_fix.xml" > "C:\MYCAT\logs\v-cats_conversion_logs-%filename%.txt"

