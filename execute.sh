#!/usr/bin/env bash

mvn compile
mvn jar:jar

echo " ------ InProc Starter ------ "
time java -cp target/360t-1.0-SNAPSHOT.jar com.graphai.InProcStarter
echo " ------ OutProc Starter ------ "
time java -cp target/360t-1.0-SNAPSHOT.jar com.graphai.OutProcStarter

