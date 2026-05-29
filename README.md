# 🎵 Spotify Music Manager

A Java desktop application for managing and analyzing Spotify music data.
Built as the final project for ITE23005 Object-Oriented Programming at SIU.

## Features
-  Secure login with BCrypt password hashing (Admin / Viewer roles)
-  Dashboard with KPI cards and 2 JFreeChart visualizations
-  Full CRUD for 3,000 Spotify tracks
-  Search and filter by genre
-  CSV export compatible with Excel
-  8/8 JUnit 5 tests passing

## Prerequisites
- Java 21+
- MySQL 9.5
- Eclipse IDE

## Setup
1. Clone this repository
2. Run `database/schema.sql` in MySQL Workbench
3. Open `SpotifyManager` in Eclipse
4. Add all JARs from `lib/` to Build Path (Classpath)
5. Run `Main.java`

## Default Accounts
| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | Admin |
| viewer | viewer123 | Viewer |

## Tech Stack
Java 21 · MySQL 9.5 · Swing · JFreeChart · BCrypt · JUnit 5

## Author
Ngo Quoc Nam — SIU 2025–2026
