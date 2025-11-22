#  Ridima Points (Spring Boot)

This is the backend  that allows users to earn and redeem
points. This system assign points to users based on the CREDIT transactions made to their wallet and  allow them to redeeem points for money to be credited to their wallet.

---

##  Tech Stack
- **Java 21**
- **Spring Boot 3**
- **Kafka**
- **Redis**
- **PostgreSQL** (Cloud-hosted in production, Docker in local development)
- **Maven** (build tool)
- **Docker & Docker Compose** (containerization & orchestration)
- **Railway** (deployment platform)

---

##  Features
- User, Wallet, Points Account Creation
- Transaction, Wallet,Points Account management
- Points Awarding and Points Redemption.
- Docker support for easy setup

---
##  System Workflow
![Entity Relationship Diagram](./architecture/erdDiagram.png)
![FlowChart](./architecture/systemFlowchart.png)

## 🛠 Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/Rhonajoy/organic-certification-backend.git
cd organic-certification-backend

### 2. Build the jar
```bash
./mvnw clean package -DskipTests
```
##  Run with Docker

```bash
    docker compose up --build
```

##  API Documentation
http://localhost:8080/swagger-ui.html

https://organic-certification-backend-production.up.railway.app/swagger-ui/index.html

## License

This project is licensed under the MIT License – free to use and modify.


