#!/bin/bash
cd /home/olanto/MYCAT/dist

java -Xmx512m -classpath "./SimpleConverter.jar" org.olanto.converter.SimpleConverterCLI /home/olanto/MYCAT/config/CONV_fix.xml


