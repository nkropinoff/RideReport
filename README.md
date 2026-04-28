# RideReport

A web platform for public transport passengers and transit operators. Passengers can leave detailed reviews of their rides; companies can register on the platform, manage their routes and fleet, and track feedback analytics across their services.

---

## Overview

RideReport bridges two audiences: everyday passengers who want to share their experience after a trip, and transport companies that need structured feedback to monitor service quality. The platform supports a full registration and moderation pipeline — companies go through an admin-reviewed approval process before gaining access to the operator panel.

---

## Features

### Passenger

- Register and authenticate via email and password
- Submit ride reviews: select a route, specify the vehicle number and ride time, write a free-form comment, and attach photos
- View personal review history with filtering options

### Company

- Submit a registration request with supporting documents attached
- Access the operator panel after admin approval
- Add and manage routes and vehicles
- View the full list of reviews linked to company services
- Inspect individual reviews in detail
- Access aggregated statistics per vehicle and route

### Administrator

- Review incoming company registration requests
- Approve or reject applications
- Download submitted company documents
- Manage company statuses through a dedicated admin panel

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 23 |
| Web layer | Jakarta Servlet API 4.0 |
| Template engine | Apache FreeMarker 2.3.34 |
| Database | PostgreSQL |
| DB access | Raw JDBC with DAO pattern |
| Password hashing | jBCrypt |
| JSON serialization | Jackson Databind 2.17 |
| Image storage | Cloudinary (SDK 2.3) |
| Logging | SLF4J + Logback |
| Build tool | Maven (WAR packaging) |
| Runtime | Any Jakarta EE-compatible servlet container (e.g. Apache Tomcat) |

---

## Architecture

The project follows a layered architecture without a framework:

```
servlets/          — HTTP request handling, split by role: /admin, /company, /passenger
services/          — Business logic
dao/               — Data access objects, raw JDBC queries
db/                — Connection pool / datasource configuration
entities/          — Domain model: User, Company, Route, Vehicle, Review, ReviewPhoto, ...
dto/               — Data transfer objects between layers
filters/           — Servlet filters (authentication, authorization by role)
listeners/         — ServletContextListener for application bootstrap
config/            — Configuration loading (database, Cloudinary)
utils/             — Shared utilities
enums/             — Role and status enumerations
exceptions/        — Custom exception hierarchy
```

Role-based access control is enforced at the filter level. Three roles exist: `PASSENGER`, `COMPANY`, and `ADMIN`. Each role maps to its own URL namespace and servlet group.

---

## Data Model

Core entities and their relationships:

- **User** — platform account, holds role and credentials
- **Company** — linked to a User, carries approval status and document references
- **CompanyDocument** — files submitted during company registration (stored on Cloudinary)
- **Route** — belongs to a Company, defined by route number and transport mode
- **TransportMode** — type of transport (bus, tram, trolleybus, etc.)
- **Vehicle** — identified by number, linked to a Route
- **Review** — written by a passenger, references a Route and a specific vehicle number, includes ride timestamp and free-form text
- **ReviewPhoto** — one or more photos attached to a Review (stored on Cloudinary)
- **City** — geographic reference used for routes

---

## Configuration

Sensitive credentials are kept outside of version control. The repository includes `.template` files as reference:

**`src/main/resources/database.properties`** — copy from `database.properties.template`:
```properties
db.url=jdbc:postgresql://localhost:5432/ridereport
db.username=your_username
db.password=your_password
db.pool.size=10
```

**`src/main/resources/cloudinary.properties`** — copy from `cloudinary.properties.template`:
```properties
cloudinary.cloud_name=your_cloud_name
cloudinary.api_key=your_api_key
cloudinary.api_secret=your_api_secret
```

---

## Getting Started

**Prerequisites:** JDK 23+, Maven 3.8+, PostgreSQL, Apache Tomcat 10+

```bash
# Clone the repository
git clone https://github.com/nkropinoff/RideReport.git
cd RideReport

# Set up configuration files
cp src/main/resources/database.properties.template src/main/resources/database.properties
cp src/main/resources/cloudinary.properties.template src/main/resources/cloudinary.properties
# Fill in your credentials in both files

# Build the WAR artifact
mvn clean package

# Deploy target/semester-work-servlets-nkropinoff-1.0-SNAPSHOT.war to Tomcat
```

After deployment the application is available at the root context of your Tomcat instance.

---

## Project Status

Developed as a semester project at Kazan Federal University (ITIS). The codebase demonstrates a production-style layered Java web application built without a framework — intentionally using the Servlet API directly to cover the fundamentals of request lifecycle, session management, and manual dependency wiring.
