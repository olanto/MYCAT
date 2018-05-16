#!/bin/bash

cd /home/olanto/MYCAT/corpus
rm -r -f txt
cp -r -f ../template/txt ./txt 

cd /home/olanto/MYCAT

rm -r -f TEMP
mkdir TEMP

rm -r -f logs
mkdir logs
