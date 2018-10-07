FROM anapsix/alpine-java:8

LABEL maintainer="Zhao Yue"

WORKDIR /app

COPY target/reverse_polish_notation_calculator-1.0-SNAPSHOT.jar /app/app.jar
COPY target/lib/ /app/lib/

CMD ["java", "-jar", "app.jar"]