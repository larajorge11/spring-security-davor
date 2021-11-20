# spring-security-davor

## Database authentication

* Alternate User details Service
* Can be in-memory, JDBC, NoSQL, external service
* Spring security provides a JDBC implementation with database schemas

## Roles vs Authorities

* Tipically a role is considered a group of one or more authorities
* Voters: Access Decision Voters provide a vote to allow access
* Role Voter
* Authenticated Voter: Anonymously, Remembered (Cookie), Fully
* Consensus Voter: Accepts list of Access Decision Voters, Polls each voter, Access granted based on total of allowed vs denied responses
* Role Hierarchy Voter: ROLE_ADMIN > ROLE_USER > ROLE_FOO
* Understand the security expressions like hasRole, hasAnyrole, hasIpAddress, etc.
* HTTP Filter Security Interceptor: Looking specific URLs depending on the configuration and then applying the voters to determine the authorization for that action
* Method Security: We have to enable the EnableGlobalMethodSecurity, under the covers Spring Security is going to be using AOP to intercept and look at that and then use the AccessDecisionManager(Same technique as Filter)
* @Secured: accept list of roles - @PreAuthorized: accept security expressions

### Spring Security JPA Auth

* Provide custom DB Auth using JPA
* Need User and Authority JPA Entities
* Spring JPA Repositories
* Custom implementation of User Details Service
* Configure Spring Security to use the custom implementation of User Detail Service

### Tips
* Don't use @Data of Lombok when there is a @ManyToMany: It will create two methods equals and hash code 
* By default, Spring Security Roles start with "ROLE_" e.g. ROLE_ADMIN