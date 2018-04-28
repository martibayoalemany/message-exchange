#!/usr/bin/env bash

mvn compile
mvn jar:jar

echo " ------ InProc Starter ------ "
time java -cp target/mmmm-SNAPSHOT.jar com.graphai.InProcStarter
echo " ------ OutProc Starter ------ "
time java -cp target/mmm-SNAPSHOT.jar com.graphai.OutProcStarter

