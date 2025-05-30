#!/bin/bash

cd "$(dirname "$0")" 

cd server

java -Xms4G -Xmx6G -jar spigot.jar nogui