#! /bin/bash

boot2docker init
boot2docker up
boot2docker shellinit
eval "$(boot2docker shellinit)"
docker build -t="codeval" .
