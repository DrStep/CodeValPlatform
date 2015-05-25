#! /bin/bash

boot2docker init
boot2docker start
eval "$(boot2docker shellinit)"
docker build -t="codeval" .
