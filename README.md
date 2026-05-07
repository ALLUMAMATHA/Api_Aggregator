# API Aggregator Dashboard

A full-stack reactive dashboard application built using Spring Boot WebFlux and React.js.  
This project aggregates user, orders, and payment information from multiple sources and displays them in a unified dashboard.

---

# Project Overview

The main goal of this project is to simulate a real-world API aggregation system.

The backend fetches data from:
- External APIs
- MySQL database

and combines everything into a single response.

The frontend displays the aggregated response in a clean dashboard UI.

---

# Features

- Reactive API aggregation using Spring WebFlux
- Fetch user details dynamically
- Fetch user orders
- Fetch payment information
- MySQL database integration
- API response caching in DB
- Exception handling
- Dynamic include parameters
- React-based dashboard UI
- REST API architecture
- Circuit Breaker configuration using Resilience4j

---

# Tech Stack

## Backend
- Java 17
- Spring Boot 3
- Spring WebFlux
- Spring Data JPA
- MySQL
- Resilience4j
- Gradle

## Frontend
- React.js
- Axios
- React Router DOM
- CSS

---

# Project Architecture

```text
React Frontend
       ↓
Spring Boot API Aggregator
       ↓
 ┌───────────────┬───────────────┬───────────────┐
 │               │               │
UserClient   OrderClient   PaymentClient
 │               │               │
External API   DB/API        DB
