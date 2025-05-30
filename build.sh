#!/bin/bash

cd "$(dirname "$0")" 

mvn install 

cp target/Arcania-1.0.jar server/plugins/Arcania.jar