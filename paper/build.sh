#!/bin/bash

cd "$(dirname "$0")" 

mvn install 

cp target/Arcania-Paper-1.0.jar ../server/paper/plugins/Arcania.jar
