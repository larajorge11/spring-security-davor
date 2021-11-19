# spring-security-davor

## Database authentication

* Alternate User details Service
* Can be in-memory, JDBC, NoSQL, external service
* Spring secutiry provides a JDBC implementation with database schemas

### Spring Secutiry JPA Auth

* Provide custom DB Auth using JPA
* Need User and Authority JPA Entities
* Spring JPA Repositores
* Custom implementation of User Details Service
* Configure Spring Secutiry to use the custom implementation of User Detail Service

### Tips
* Don't use @Data of Lombok when there is a @ManyToMany: It will create two methods equals and hash code 