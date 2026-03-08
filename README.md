# 🏏 IPL Player Transfer Management System

A multi-client **IPL Player Transfer Management System** built with **JavaFX** and **Java Socket Programming** as part of the L-1 T-2 course project.

---

## 📋 Features

| Role | Capabilities |
|------|-------------|
| **Server (Admin)** | Start server, add players, monitor all connected clubs, view transfer log |
| **Club Manager** | Buy players from other clubs, list own players for sale, view squad stats |
| **Viewer (User)** | Browse all players, filter by country/club/position, view live statistics |

- 🔒 **Secure login** — passwords hashed with SHA-256 (never stored in plaintext)
- 🔄 **Real-time updates** — all connected clients notified instantly on transfers
- 💾 **File-based persistence** — player data saved automatically every 5 minutes
- 🔎 **Live search** — filter players by name, country, position, and more

---

## 🛠️ Prerequisites

| Tool | Version |
|------|---------|
| Java JDK | 21+ |
| Apache Maven | 3.8+ |

> **Check your versions:**
> ```bash
> java -version
> mvn -version
> ```

---

## 🚀 How to Run

> ⚠️ **Start the Server first, then open Client windows.**

### 1. Clone the repository
```bash
git clone https://github.com/YOUR_USERNAME/ipl-player-manager.git
cd ipl-player-manager
```

### 2. Compile the project
```bash
mvn clean compile
```

### 3. Start the Server (Terminal 1)
```bash
mvn javafx:run@server
```

### 4. Start a Client / Viewer (Terminal 2, 3, ...)
```bash
mvn javafx:run@client
```

---

## 🔑 Default Credentials

All default passwords are: **`1234`**

**Club Managers:**
- Mumbai Indians, Chennai Super Kings, Royal Challengers Bangalore
- Rajasthan Royals, Gujarat Titans, Delhi Capitals, Punjab Kings
- Kolkata Knight Riders, Sunrisers Hyderabad, Lucknow Super Giants

**Viewers (Users):**
- Usha, Maruf, Tamim, Turab, Mosambika

---

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/com/example/demonew/
│   │   ├── server/          # Server app, socket handling, Player model
│   │   ├── login/           # Login & signup for both roles
│   │   ├── updatedClient/   # Club Manager dashboard, buy/sell
│   │   ├── updatedViewer/   # Viewer dashboard, stats, filters
│   │   └── util/            # PasswordUtil (SHA-256), AlertUtil
│   └── resources/
│       ├── players.txt      # Player data store
│       ├── login/           # Credential files (hashed passwords)
│       └── Images/          # UI assets
```

---

## 🔧 Architecture

```
[LoginApp] ──login──► [Server: port 44444]
                            │
              ┌─────────────┼─────────────┐
              │             │             │
       [Club A]       [Club B]       [Viewer]
   (buy/sell)      (buy/sell)    (read-only)
```

- Server runs a `ServerSocket` on port **44444**
- Each client gets a dedicated thread via `SocketWrapper`
- All player data is kept in a synchronized in-memory list and flushed to `players.txt`
- Password authentication uses **SHA-256** hashing

---

## 📦 Built With

- [JavaFX 21](https://openjfx.io/) — UI framework
- [Java Sockets](https://docs.oracle.com/en/java/docs/) — Networking
- [Maven](https://maven.apache.org/) — Build system

---

## 👤 Author

**Student ID: 2205065** | L-1 T-2 Project
