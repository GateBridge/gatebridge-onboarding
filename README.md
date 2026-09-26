# 🌉 GateBridge — Minimal Onboarding Starter

[![License: Apache 2.0](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE)
[![JitPack](https://img.shields.io/badge/JitPack-gatebridge--core-brightgreen)](https://jitpack.io/#GateBridge/gatebridge)
[![Java 21+](https://img.shields.io/badge/Java-21%2B-orange.svg)](https://openjdk.org/projects/jdk/21/)

**GateBridge Onboarding Starter** is the official minimal, functional boilerplate application for **[GateBridge API Gateway](https://github.com/GateBridge/gatebridge)**.

Whenever you want to launch GateBridge in a new environment, cloud provider, or production server, simply **clone this repository** as your baseline starter project and extend it.

---

## ⚡ Quickstart (60 Seconds)

### 1. Run Interactive DevOps Mode (With TUI Dashboard)
```bash
./run.sh
```

### 2. Run Production Headless Mode (CLI / Background)
```bash
./run_headless.sh
```

### 3. Run with Docker Compose
```bash
docker compose up -d
```

---

## 🧪 Verify Endpoints

Once launched, test your gateway endpoints:

```bash
# 1. Fast-Path Health Check (System Route)
curl http://localhost:4000/v1/health

# 2. Fast-Path Ping Endpoint
curl http://localhost:4000/v1/ping

# 3. Admin Management Node List
curl http://localhost:9090/v1/get_nodes
```

---

## 📦 How Dependency Resolution Works

This onboarding template depends directly on `gatebridge-core` via Maven (`pom.xml`):

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>io.hexacloud</groupId>
        <artifactId>gatebridge-core</artifactId>
        <version>1.4.9-release</version>
    </dependency>
</dependencies>
```

When building, Maven fetches the latest `gatebridge-core` release binary automatically from **JitPack** or your local `~/.m2` repository.

---

## 🛠️ Project Structure

```
gatebridge-onboarding/
├── Dockerfile                   # Multi-stage Docker deployment
├── docker-compose.yml           # Production Docker compose manifest
├── pom.xml                      # Maven configuration with JitPack repository
├── run.sh                       # Start Gateway with interactive TUI
├── run_headless.sh              # Start Gateway in headless mode
└── src/main/java/io/gatebridge/onboarding/
    ├── OnboardingApplication.java   # Main entry point & GatewayBuilder config
    ├── controller/
    │   └── HealthCheckController.java # Fast-path custom RouteController (@RouteMapping)
    └── listener/
        └── TelemetryEventListener.java # Real-time EventBus listener (@Subscribe)
```

---

## 🚀 Customizing Your Gateway

To adapt this starter kit for your own microservices infrastructure:

1. Open `src/main/java/io/gatebridge/onboarding/OnboardingApplication.java`.
2. Map your backend target services:
   ```java
   builder.createCluster("payments-cluster")
          .registerServer("http://payments-service.internal", 8080);
   ```
3. Add custom route controllers implementing `RouteController` with `@RouteMapping`.
4. Deploy using `./run_headless.sh` or `docker compose up -d`.
