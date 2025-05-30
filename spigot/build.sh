#!/bin/bash

cd "$(dirname "$0")" 

mvn install 

cp target/Arcania-Spigot-1.0.jar ../server/spigot/plugins/Arcania.jar