FROM adoptopenjdk:15

LABEL qontakt=internal

ENTRYPOINT java --enable-preview -jar /app/app.jar

EXPOSE 8080

COPY app.jar /app/app.jar