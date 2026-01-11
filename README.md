# Word Learning App

A personal vocabulary-building tool for reading books in English. Capture unfamiliar words while reading, let the system enrich them automatically, then learn them later via flashcards.

## Problem

Reading in English with unfamiliar words is frustrating: stopping to translate breaks flow, skipping leads to confusion. This app lets you capture words quickly and learn them separately.

## How It Works

1. **Capture** — Add words while reading (with optional context sentence)
2. **Process** — Background job enriches words via AI (meaning, translation, commonness)
3. **Learn** — Review enriched words in flashcard sessions, mark as learned or discard

## Tech Stack

* Java 21
* Spring Boot
* PostgreSQL
* Flyway (schema-first migrations)
* Thymeleaf (server-rendered UI)
* MapStruct (DTO mapping)

## Local Setup

```bash
# Start database
docker-compose up -d

# Run application
./gradlew bootRun

# Open browser
open http://localhost:8080
```

## Database Schema

| Table | Purpose |
|-------|---------|
| `word` | Captured words with processing/learning status |
| `word_enrichment` | AI-generated data (meaning, translation, commonness) |
| `category` | Fixed list of broad topics (Food, Travel, Work, etc.) |
| `word_group` | Dynamic semantic clusters within categories |

Schema is managed by Flyway. JPA is set to `validate` mode (no auto-DDL).

## Configuration

Default config connects to local Postgres via docker-compose. For custom settings, create `application-local.yml` (gitignored).