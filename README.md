# microservices

Project containing six micoroservices written in Java, three MYSQL databases and three MONGO databases. Everthing uder controll of Eureka Server. Two business logic services, API gateway with sercurity and configuration server with three different configurations on GitHub.

To build project use:
>> mvn clean install -DskipTests

To run project use:
>> docker-compose up -d --build

To make API requests use file: microservices.postman_collection.json

To check out services status:
>> localhost:8761
