#  Spotify Music Manager

> A Java desktop application for managing and analyzing Spotify music data.  
> Built as the Final Project for **ITE23005 — Object-Oriented Programming with Java**  
> The Saigon International University | Semester 2, Academic Year 2025–2026

---

## 📋 Project Overview

Spotify Music Manager is a fully featured desktop application that allows admin and viewer users to explore, manage, and visualize a curated catalog of 3,000 Spotify tracks across 20 genres. The application was developed using core Java OOP principles, design patterns, and modern Java 21 features.

---

##  Features

###  Authentication & Security
- Login / Logout with session management
- Passwords hashed using **BCrypt** before storage
- Two roles: **Admin** (full CRUD) and **Viewer** (read-only)
- Last login timestamp recorded on each successful login

###  Dashboard
- KPI cards: Total Tracks, Total Genres, Current Role
- **Bar Chart**: Top 10 Genres by Average Popularity (JFreeChart)
- **Pie Chart**: Track Distribution by Genre (JFreeChart)
- Genre statistics table powered by Java Stream API

###  Track Management
- View all 3,000 tracks in a sortable JTable
- **Search** by track name or artist name
- **Filter** by genre using dropdown
- **Add / Edit / Delete** records with form validation (Admin only)
- **Export to CSV** — opens in Excel with correct UTF-8 encoding

###  Unit Testing
- 8 JUnit 5 test methods — all passing 
- Tests cover: filtering, sorting, Optional, Switch expression, Record validation

---

##  Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 21 (LTS) | Core language |
| Eclipse IDE | 2026-03 | Development environment |
| MySQL | 9.5 Community | Relational database |
| Java Swing | Built-in | Desktop GUI |
| JFreeChart | 1.0.19 | Charts and visualizations |
| jBCrypt | 0.4 | Password hashing |
| JUnit | 5.10.2 | Unit testing |
| Git / GitHub | 2.54 | Version control |

---

##  Database Schema

The database contains **6 related tables**:
