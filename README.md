# CampusConnect

A full-stack campus event management application built with React, Spring Boot, and MySQL.

> **Project status:** MVP with event management and attendee registration APIs. Login, verified user accounts, organizer/admin permissions, and production hosting remain future work.

## Stack

- Frontend: React, Vite, JavaScript
- Backend: Java 17+, Spring Boot, Spring Data JPA, Bean Validation
- Database: MySQL 8
- Local database: Docker Compose

## Features in this milestone

- Create campus events
- View all events
- Edit event details
- Delete events
- Validate required event fields
- Register attendees using name and email
- Prevent duplicate registrations for the same event/email
- View attendee counts and registration records
- Cancel an event registration
- Persist events and registrations in MySQL

## Run locally

### 1. Start MySQL

From the repository root:

```bash
docker compose up -d
```

The development database is exposed on port `3307` and maps to MySQL's internal port `3306`.

### 2. Start the backend

Requires Java 17+ and Maven.

```bash
cd backend
mvn spring-boot:run
```

API base URL: `http://localhost:8080/api/events`

### 3. Start the frontend

In a second terminal:

```bash
cd frontend
npm install
npm run dev
```

Open the Vite URL shown in the terminal (normally `http://localhost:5173`).

## API

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/events` | List events |
| GET | `/api/events/{id}` | Get one event |
| POST | `/api/events` | Create an event |
| PUT | `/api/events/{id}` | Update an event |
| DELETE | `/api/events/{id}` | Delete an event |
| POST | `/api/events/{eventId}/registrations` | Register an attendee |
| GET | `/api/events/{eventId}/registrations` | List registrations (name and registration time; email is not returned) |
| GET | `/api/events/{eventId}/registrations/count` | Get attendee count |
| DELETE | `/api/events/{eventId}/registrations/{registrationId}` | Cancel a registration |

Example request body:

```json
{
  "title": "Tech Fest",
  "description": "Annual campus technology festival",
  "venue": "Main Auditorium",
  "eventDate": "2027-02-15T10:00:00"
}
```

## Configuration

The backend reads database settings from environment variables. Defaults are intended only for local development:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

Do not use development credentials in production. Keep real secrets out of Git.

## Repository structure

```text
CampusConnect/
├── backend/          # Spring Boot REST API
├── frontend/         # React + Vite client
├── .github/workflows # CI checks
└── docker-compose.yml
```

## Roadmap

- [x] Initial React + Spring Boot + MySQL scaffold
- [x] Event CRUD API and UI
- [x] Event attendee registration API with duplicate prevention
- [ ] Automated end-to-end frontend tests
- [ ] User accounts and secure authentication
- [ ] Admin and organizer roles with authorization
- [ ] Production API hosting, secrets, and deployment configuration

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).

## License

No license has been selected yet. All rights reserved by default; ask the repository owner before reusing this project.
