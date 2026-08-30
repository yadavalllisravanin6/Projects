# 🤖 AI Code Review Bot

A **Fullstack Java** project that automatically reviews GitHub Pull Requests using **Claude AI**.

When a developer opens a PR → GitHub sends a webhook → Spring Boot fetches the diff → Claude AI reviews the code → Bot posts a comment on the PR.

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21 + Spring Boot 3 |
| AI | Claude API (Anthropic) |
| Database | PostgreSQL (Supabase - free) |
| Frontend | React 18 |
| Deployment | Railway (free tier) |

---

## ⚙️ Setup Guide (Step by Step)

### Step 1 — Get Your Free API Keys

**Claude API Key (Free tier):**
1. Go to https://console.anthropic.com
2. Sign up → API Keys → Create Key
3. Copy the key

**GitHub Personal Access Token:**
1. GitHub → Settings → Developer Settings → Personal Access Tokens → Fine-grained tokens
2. Give it `pull_requests: read/write` and `issues: write` permissions
3. Copy the token

**Supabase (Free PostgreSQL):**
1. Go to https://supabase.com → New Project
2. Copy the connection string from Settings → Database

---

### Step 2 — Configure the App

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://db.YOUR_ID.supabase.co:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

claude.api.key=YOUR_CLAUDE_API_KEY
github.token=YOUR_GITHUB_TOKEN
github.webhook.secret=any-random-string-you-choose
```

---

### Step 3 — Run Locally

```bash
# Backend
./mvnw spring-boot:run

# Frontend (new terminal)
cd frontend
npm install
npm start
```

Backend runs at: http://localhost:8080
Frontend runs at: http://localhost:3000

---

### Step 4 — Expose Your Local Server (for GitHub Webhook)

While developing locally, use ngrok to expose your server:

```bash
# Install ngrok (free): https://ngrok.com
ngrok http 8080
```

Copy the HTTPS URL, e.g. `https://abc123.ngrok.io`

---

### Step 5 — Set Up GitHub Webhook

1. Go to your GitHub repo → **Settings → Webhooks → Add webhook**
2. **Payload URL:** `https://abc123.ngrok.io/api/webhook/github`
3. **Content type:** `application/json`
4. **Events:** Select "Pull requests" only
5. Click **Add webhook**

---

### Step 6 — Test It!

Open a Pull Request in your repo. Within seconds, the bot will post an AI review comment!

---

### Step 7 — Deploy to Railway (Free)

```bash
# Install Railway CLI
npm install -g @railway/cli

# Login and deploy
railway login
railway init
railway up
```

Set your environment variables in Railway dashboard, then update the GitHub webhook URL to your Railway URL.

---

## 📁 Project Structure

```
ai-code-review-bot/
├── src/main/java/com/aireviewer/
│   ├── AiCodeReviewBotApplication.java   ← Entry point
│   ├── config/
│   │   ├── WebClientConfig.java          ← HTTP clients setup
│   │   └── AsyncConfig.java              ← Background processing
│   ├── controller/
│   │   ├── WebhookController.java        ← Receives GitHub webhooks
│   │   └── ReviewController.java        ← Dashboard API
│   ├── service/
│   │   ├── CodeReviewService.java        ← Orchestrates the flow
│   │   ├── ClaudeAiService.java          ← Calls Claude API
│   │   └── GitHubService.java            ← Fetches diffs, posts comments
│   ├── model/
│   │   └── PullReview.java              ← Database entity
│   ├── repository/
│   │   └── PullReviewRepository.java    ← JPA queries
│   └── dto/
│       ├── GitHubWebhookPayload.java    ← GitHub payload parsing
│       ├── ClaudeRequest.java           ← Claude API request
│       └── ClaudeResponse.java         ← Claude API response
├── frontend/
│   └── src/App.jsx                      ← React dashboard
├── Dockerfile                           ← For Railway deployment
└── pom.xml                             ← Maven dependencies
```

---

## 🔄 How It Works (Flow)

```
Developer opens PR
      ↓
GitHub sends webhook POST to /api/webhook/github
      ↓
WebhookController receives it → responds 200 immediately
      ↓
CodeReviewService runs ASYNC in background
      ↓
GitHubService fetches the PR diff
      ↓
ClaudeAiService sends diff to Claude API
      ↓
Claude returns code review in Markdown
      ↓
GitHubService posts comment on the PR
      ↓
Review saved to PostgreSQL database
      ↓
Dashboard shows review history
```

---

## API Endpoints

| Method | URL | Description |
|---|---|---|
| POST | `/api/webhook/github` | GitHub webhook receiver |
| GET | `/api/webhook/health` | Health check |
| GET | `/api/reviews/recent` | Last 10 reviews |
| GET | `/api/reviews/repo?name=owner/repo` | Reviews by repo |
