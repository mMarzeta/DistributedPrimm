#!/usr/bin/env bash

javac client/*.java
jar cvf client.jar client/*.class
javac -cp client.jar server1/*.java server2/*.java client/*.java

java -cp . \
    -Djava.rmi.server.codebase=file:client.jar \
    -Djava.security.policy=application.policy \
    server1/Server &

java -cp . \
    -Djava.rmi.server.codebase=file:client.jar \
    -Djava.security.policy=application.policy \
    server2/Server &

java -cp . \
    -Djava.rmi.server.codebase=file:client.jar \
    -Djava.security.policy=application.policy \
    client/Prim 127.0.0.1