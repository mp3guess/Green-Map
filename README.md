Green Map 🌍
Green Map is pet project aimed at promoting environmental awareness by mapping green spaces, eco-friendly businesses, and sustainable living resources in local communities. The project empowers users to discover and share information about parks, nature reserves, recycling centers, organic stores, renewable energy resources, and other sustainability-focused spots.

Features
🗺️ Interactive Map: Explore local green spaces, eco-friendly shops, and sustainability initiatives.
📍 Add Locations: Users can contribute by adding new green spaces or businesses.
🔎 Search: Easily search for specific locations or sustainability categories (e.g., recycling centers, organic markets).
🔧 Customization: Customize the map view based on environmental categories.

Technologies Used:

Spring Boot (v3.3.1): Used for building the backend services and handling web requests.
Spring Security (v6.1.3): Used for securing the application, including authentication and authorization.
Spring Data JPA: For database interaction using Java Persistence API.
PostgreSQL: As the relational database system.
MapStruct (v1.5.5.Final): For mapping DTOs and entities.
JWT (JSON Web Token): Used for secure token-based authentication.
Redis: For caching and improving application performance.
Liquibase: Database migration management tool.
Spring Mail: For email services.

Build Tools and Plugins:
Java (17)
Gradle: Used as the build automation tool.
Checkstyle (v10.3.1): For ensuring consistent code quality.
Jacoco: For code coverage reporting.
Docker (v3.3.6): To containerize the application for easier deployment.

Testing:
JUnit Platform: For unit and integration testing.
Spring Security Test: For testing security-related functionality.

Logging:
Log4j (v1.2.17): For logging and monitoring application behavior.

API Documentation:
Swagger (v2.2.0): For generating interactive API documentation.

Getting Started Steps:
1) Run docker-compose up while having your Docker Application Running
2) Run ./gradlew build
3) Run ./gradlew bootRun
4) Access the endpoints using the documentation in the following URL: http://localhost:8080/swagger-ui.html
