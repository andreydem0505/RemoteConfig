FROM openjdk:25-jdk-slim
ARG JAR_FILE=build/libs/*.jar

# Install curl for healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

ENV JAVA_OPTS="\
-XX:ActiveProcessorCount=6 \
-XX:+UseContainerSupport \
-XX:+UseG1GC \
-XX:InitiatingHeapOccupancyPercent=35 \
-XX:MaxGCPauseMillis=150 \
-XX:ConcGCThreads=2 \
-XX:ParallelGCThreads=4 \
-Xms4g \
-Xmx4g \
-XX:+AlwaysPreTouch \
-XX:+PerfDisableSharedMem \
-XX:+ExitOnOutOfMemoryError \
-XX:+FlightRecorder \
-XX:StartFlightRecording=duration=120s,filename=/snapshots/profile.jfr"

COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT exec java ${JAVA_OPTS} -jar /app.jar