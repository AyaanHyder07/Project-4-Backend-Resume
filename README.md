# Resume Dashboard Backend

Spring Boot 3.2.3 REST API for the Resume Dashboard SaaS.

## Requirements
- Java 17
- Maven
- MongoDB running at localhost:27017

## Run
```bash
mvn spring-boot:run
```

Server starts on http://localhost:8080

## Create Admin User
Manually insert an admin user in MongoDB:

```javascript
// In mongosh or Compass
use resume_dashboard
db.users.insertOne({
  email: "admin@example.com",
  password: "$2a$10$...",  // BCrypt hash of your password
  role: "ROLE_ADMIN",
  status: "ACTIVE",
  createdAt: new Date()
})
```

To generate a BCrypt hash, use an online tool or:
```java
// In a simple Java main or test
String hash = new BCryptPasswordEncoder().encode("yourpassword");
```
