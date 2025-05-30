#!/bin/bash

cd "$(dirname "$0")" 

curl -O https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar

java -jar BuildTools.jar --rev 1.21.5

cp spigot-1.21.5.jar ../server/spigot.jar

echo "Spigot installed successfully, make sure you run the start.sh script to create the necessary plugins folder before building the plugin."