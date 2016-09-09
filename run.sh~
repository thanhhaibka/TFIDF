#!/bin/bash
script_path=`dirname $script`
cd $script_path

pathrunjob=/home/haint/TFIDF
timesleep=10m

for (( ; ; ));
do
  hour=`date +"%H"`
  cd $pathrunjob
  java -Xms812M -Xmx3G $param -classpath dist/*:*  app.Token
  echo -e "Current time: `date`"
  echo -e "Sleep $timesleep ..."
  sleep $timesleep
done
