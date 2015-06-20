FROM ubuntu:latest
RUN apt-get update
RUN apt-get install gcc -y --force-yes
RUN apt-get install fp-compiler
RUN apt-get install ruby-full
