# avrioc-student
Student management module for Avrioc 

Tech Stacks 
 
 Java 11 - Version 11 of java as programming language,
 Spring Boot - Application framework,
 Spring Boot Web Flux - For reactive programming,
 Spring Security - for security using JWT,
 Spring Bean Validator - for validating incoming request,
 Mongo DB - Database used,
 Spring Data MongoDB - ORM framework,
 Lombok - Annotation based java library for reducing boiler plate code,
 Junit 5 - Testing framework
 
 WorkFlow
 
 Login API --> Create Student --> Add Student Result 
 Student's Result can be added in a web browser page/any subscriber clients.
 The response will have the latest position(rank) of the student in class.
 
 Login API --> Get Student Result 
 
 Login API --> Get all students result
 
 Login API --> Delete Student
 
 Login API creates JWT token used for authorization.
 Based on the user, token will be generated.
 Use the token to access the API's of the application.
 
 
 
postman collection having all the API's is uploaded in this git repository project.
 
 
