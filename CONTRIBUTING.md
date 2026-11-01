# Contributing

Thanks for your interest in CampusConnect.

## Development setup

1. Install Java 17+, Maven, Node.js 20+, npm, and Docker.
2. Start MySQL with `docker compose up -d`.
3. Run the backend from `backend/` using `mvn spring-boot:run`.
4. Run the frontend from `frontend/` using `npm install` and `npm run dev`.

## Guidelines

- Keep changes focused and explain the reason in the pull request.
- Validate API input and return useful HTTP status codes.
- Never commit credentials, database dumps, build output, or `.env` files.
- Update the README when setup or API behavior changes.
- Add tests for new behavior as the test suite is introduced.

## Pull requests

Include a short summary, steps to verify the change, and screenshots for visible UI changes.
