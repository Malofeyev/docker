FROM adoptopenjdk/maven-openjdk11
COPY ../market/target/docker/market/1.0-SNAPSHOT/build/maven /maven/
CMD ["java","-jar","maven/market-1.0-SNAPSHOT.jar"]
