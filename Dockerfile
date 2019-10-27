FROM openjdk:8-alpine

MAINTAINER Mojammal Hock <mojammal.hock@gmail.com>

ENV HOME /home/javabatch/run
ENV RESULT /home/javabatch/result
ENV TZ=Europe/Berlin

ARG NUMBEROFFILES
ENV NUMBEROFFILES=${NUMBEROFFILES:-50}
ARG THREADCOUNT
ENV THREADCOUNT=${THREADCOUNT:-5}

RUN apk --update add --no-cache bash vim \
        && ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR $HOME
COPY target/chunkProcessing.jar $HOME/chunkProcessing.jar
COPY target/lib/* $HOME/lib/

#ADD HelloWorld.jar HelloWorld.jar
#EXPOSE 8080
#CMD java -jar HelloWorld.jar

EXPOSE 8080 8000

#ENTRYPOINT ["java", "-jar", "batchProcessing.jar", "batchlet-example", "numberOfFiles=$NUMBEROFFILES", "threadCount=$THREADCOUNT"]
CMD java -jar batchProcessing.jar chunk-single-writer numberOfFiles=$NUMBEROFFILES threadCount=$THREADCOUNT
