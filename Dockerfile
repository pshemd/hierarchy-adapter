FROM openjdk:11-jre-slim
COPY /build/libs/mdm-objects-service-0.1.jar .
EXPOSE 8097
ENTRYPOINT ["java","-jar","mdm-objects-service-0.1.jar"]