@echo on


set JAVA_HOME=C:\Program Files\Java\jdk1.6.0_23
set JRE_HOME=C:\Program Files\Java\jre6

set DIST=C:\MYPREP\prog\SimpleConverter\dist
set LIB=%DIST%\lib

set CLASSPATH=%DIST%\SimpleConverter.jar
set CLASSPATH=%CLASSPATH%;%LIB%\avalon-framework-api-4.3.1.jar
set CLASSPATH=%CLASSPATH%;%LIB%\bcmail-jdk16-145.jar
set CLASSPATH=%CLASSPATH%;%LIB%\bcprov-jdk16-145.jar
set CLASSPATH=%CLASSPATH%;%LIB%\bootstrapconnector.jar
set CLASSPATH=%CLASSPATH%;%LIB%\commons-cli-1.2.jar
set CLASSPATH=%CLASSPATH%;%LIB%\commons-codec-1.3.jar
set CLASSPATH=%CLASSPATH%;%LIB%\commons-io-1.3.1.jar
set CLASSPATH=%CLASSPATH%;%LIB%\commons-io-1.4.jar
set CLASSPATH=%CLASSPATH%;%LIB%\commons-lang-2.4.jar
set CLASSPATH=%CLASSPATH%;%LIB%\commons-logging-1.1.1.jar
set CLASSPATH=%CLASSPATH%;%LIB%\commons-vfs-patched-1.9.1.jar
set CLASSPATH=%CLASSPATH%;%LIB%\docx4j-2.6.0.jar
set CLASSPATH=%CLASSPATH%;%LIB%\FontBox-0.1.0-dev.jar
set CLASSPATH=%CLASSPATH%;%LIB%\fop-1.0.jar
set CLASSPATH=%CLASSPATH%;%LIB%\hwpf-3.4.0.jar
set CLASSPATH=%CLASSPATH%;%LIB%\jdom-1.0.jar
set CLASSPATH=%CLASSPATH%;%LIB%\jodconverter-cli-2.2.2.jar
set CLASSPATH=%CLASSPATH%;%LIB%\jodconverter-core-3.0-beta-3.jar
set CLASSPATH=%CLASSPATH%;%LIB%\juh-3.0.1.jar
set CLASSPATH=%CLASSPATH%;%LIB%\jurt-3.0.1.jar
set CLASSPATH=%CLASSPATH%;%LIB%\log4j-1.2.16.jar
set CLASSPATH=%CLASSPATH%;%LIB%\PDFBox-0.7.3.jar
set CLASSPATH=%CLASSPATH%;%LIB%\ridl-3.0.1.jar
set CLASSPATH=%CLASSPATH%;%LIB%\serializer-2.7.1.jar
set CLASSPATH=%CLASSPATH%;%LIB%\slf4j-api-1.5.6.jar
set CLASSPATH=%CLASSPATH%;%LIB%\slf4j-jdk14-1.5.6.jar
set CLASSPATH=%CLASSPATH%;%LIB%\unoil-3.0.1.jar
set CLASSPATH=%CLASSPATH%;%LIB%\wmf2svg-0.8.3.jar
set CLASSPATH=%CLASSPATH%;%LIB%\xalan-2.7.1.jar
set CLASSPATH=%CLASSPATH%;%LIB%\xml-apis-1.3.04.jar
set CLASSPATH=%CLASSPATH%;%LIB%\xmlgraphics-commons-1.4.jar
set CLASSPATH=%CLASSPATH%;%LIB%\xstream-1.3.1.jar

java -Xmx512m -cp "%CLASSPATH%" com.simple.converter.SimpleConverterApplicationTest -f txt -b D:/PREP/BAD D:/PREP/TO_BE_CONVERTED D:/PREP/CONVERTED

@echo on