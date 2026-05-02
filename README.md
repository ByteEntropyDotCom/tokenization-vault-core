# 🛡️ Tokenization-Vault-Core
A high-performance, low-latency microservice designed for Format-Preserving Encryption (FPE) of Primary Account Numbers (PANs). This service is a critical component of a modern payment switch, ensuring that sensitive card data is never stored in its raw form.

## 🚀 Performance & Tech Stack
* Java 21: Leveraging Virtual Threads (Project Loom) for massive concurrency.
* Spring Boot 3.2+: Configured for non-blocking I/O.
* FF1/AES: Format-Preserving Encryption via Bouncy Castle.
* Zero-Lombok: Clean, transparent bytecode for mission-critical reliability.
* ZGC: Low-latency Garbage Collector for sub-millisecond pause times.

## 🛠️ System Architecture
The vault acts as a "Security Fortress" between the incoming ISO-8583 Adapter and the Card-Lifecycle Engine.

1. Request: Receives a 16-digit PAN.

2. Encryption: Uses FF1 algorithm to transform data while keeping it numeric and 16 digits long.

3. Persistence: Maps PAN ↔ Token in a high-speed repository.

4. Response: Returns a token and a unique traceId for audit logging.

## 📋 Features
1. Format Preservation: 4111 2222 3333 4444 → 1759 0860 6137 7590.

2. PCI-DSS Scope Reduction: Systems outside the vault only handle tokens, removing them from expensive compliance audits.

3. Virtual Thread Scaling: Capable of handling 10k+ TPS on modest hardware.

4. Tweakable Encryption: Uses the first 6 digits (BIN) as a "tweak" for unique encryption patterns per card range.

## 🚦 Getting Started

### Prerequisites
* Java 21 JDK
* Maven 3.9+
* Docker (Optional)

### Installation

```
Bash
git clone https://github.com/your-repo/tokenization-vault-core.git
cd tokenization-vault-core
mvn clean install
```

### Running Locally

```Bash
# Set your master key (16, 24, or 32 bytes)
export VAULT_MASTER_KEY="M250cjBweV9WNHVsdF8yMQ==" 
mvn spring-boot:run
```

### Testing the API


```
Bash
curl -X POST http://localhost:8081/api/v1/vault/tokenize \
     -H "Content-Type: application/json" \
     -d '{"pan":"4111222233334444", "requestContext":"postman-test"}'
```

###  🐳 Docker Deployment
The project includes a multi-stage, non-root Alpine Dockerfile optimized for ZGC.

```Bash
docker build -t vault-core .
docker run -p 8081:8081 -e VAULT_MASTER_KEY="..." vault-core
```

## LICENCE
MIT