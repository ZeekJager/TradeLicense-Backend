# Trade License Workflow Frontend

React dashboard for the Trade License backend in this repository.

## Stack

- Vite + React + TypeScript
- React Router
- Redux Toolkit + RTK Query
- Plain CSS, no external UI framework

## Environment

Create `frontend/.env` if the backend URL differs:

```bash
VITE_API_BASE_URL=http://localhost:8080/api
```

## Run

Start the backend from the repository root:

```bash
./mvnw spring-boot:run
```

Start the frontend:

```bash
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`.

## Seeded Accounts

All seeded accounts use password `password`.

- `admin@tradelicense.test`
- `customer@tradelicense.test`
- `reviewer@tradelicense.test`
- `approver@tradelicense.test`

## Main Routes

- `/customer`: create application, upload required documents, upload bank slip, submit/cancel
- `/reviewer`: review submitted applications
- `/approver`: approve reviewed applications
- `/admin`: users, roles, status monitoring, license type config, reports

## API Integration

RTK Query services live in `src/services` and map to backend routes:

- Auth: `/api/auth/login`, `/api/auth/me`, `/api/auth/logout`
- Applications: `/api/trade-license-applications`
- Review: `/api/trade-license-reviews`
- Approval: `/api/trade-license-approvals`
- Files: `/api/trade-license-applications/{id}/documents/upload`, `/payment/slip`
- Admin: `/api/admin`
