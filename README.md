# IPL Player Transfer Management System 🏏

A multi-client real-time player transfer platform for IPL teams — built with **JavaFX** and **Java Socket Programming** as a Level 1 Term 2 project at BUET.

## 🎬 Video Demonstration

[![IPL Demo](https://img.shields.io/badge/YouTube-Demo%20Video-red?style=for-the-badge&logo=youtube)](#)

> **Add your demo link here**

---

## 📌 About

A networked desktop application where club managers can buy and sell IPL players in real-time across multiple connected clients. The server acts as admin, managing the player pool and broadcasting live transfer updates to all connected sessions simultaneously.

---

## ✨ Features

| Role | Capabilities |
|---|---|
| 🖥️ **Server (Admin)** | Start server, add players, monitor all connected clubs, view full transfer log |
| 🏟️ **Club Manager** | Buy players from other clubs, list own players for sale, view squad stats |
| 👁️ **Viewer** | Browse all players, filter by country/club/position, view live statistics |

- 🔒 **Secure login** — passwords hashed with SHA-256, never stored in plaintext
- 🔄 **Real-time updates** — all connected clients notified instantly on every transfer
- 💾 **Persistent storage** — player data auto-saved every 5 minutes to `players.txt`
- 🔎 **Live search** — filter players by name, country, position, and club
- 🧵 **Multi-threaded** — each client runs on a dedicated server thread via `SocketWrapper`

---

## 🗂 Project Structure

```
src/
└── main/
    ├── java/com/example/demonew/
    │   ├── server/           # Server app, socket handling, Player model
    │   ├── login/            # Login & signup for Club Managers and Viewers
    │   ├── updatedClient/    # Club Manager dashboard — buy/sell interface
    │   ├── updatedViewer/    # Viewer dashboard — stats and filters
    │   └── util/             # PasswordUtil (SHA-256), AlertUtil
    └── resources/
        ├── players.txt       # Player data store
        ├── login/            # Credential files (hashed passwords)
        └── Images/           # UI assets
```

---

## 🔧 Architecture

```
[LoginApp] ──login──► [Server: port 44444]
                              │
                ┌─────────────┼─────────────┐
                │             │             │
          [Club A]       [Club B]       [Viewer]
        (buy/sell)     (buy/sell)    (read-only)
```

- Server runs a `ServerSocket` on port **44444**
- Each client gets a dedicated thread via `SocketWrapper`
- Player data is held in a synchronized in-memory list, flushed to `players.txt`
- All transfers broadcast instantly to every connected client

---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| UI Framework | JavaFX 21 |
| Networking | Java Socket Programming |
| Authentication | SHA-256 password hashing |
| Persistence | File-based storage (`players.txt`) |
| Build System | Apache Maven 3.8+ |
| Language | Java 21 |

---

## ⚙️ Prerequisites

- **Java JDK 21+**
- **Apache Maven 3.8+**

Verify your versions:
```bash
java -version
mvn -version
```

---

## 🚀 Setup & Installation

### 1. Clone the repository

```bash
git clone https://github.com/Usha0070/ipl-player-manager.git
cd ipl-player-manager
```

### 2. Compile the project

```bash
mvn clean compile
```

### 3. Start the Server

> ⚠️ Always start the server **before** opening any client windows.

```bash
# Terminal 1 — Server
mvn javafx:run@server
```

### 4. Start Client / Viewer sessions

```bash
# Terminal 2, 3, ... — Client or Viewer
mvn javafx:run@client
```

---

## 🔑 Default Credentials

All default passwords are: **`1234`**

**Club Managers**

| Club | Username |
|---|---|
| Mumbai Indians | Mumbai Indians |
| Chennai Super Kings | Chennai Super Kings |
| Royal Challengers Bangalore | Royal Challengers Bangalore |
| Rajasthan Royals | Rajasthan Royals |
| Gujarat Titans | Gujarat Titans |
| Delhi Capitals | Delhi Capitals |
| Punjab Kings | Punjab Kings |
| Kolkata Knight Riders | Kolkata Knight Riders |
| Sunrisers Hyderabad | Sunrisers Hyderabad |
| Lucknow Super Giants | Lucknow Super Giants |

**Viewers**

`Usha` · `Maruf` · `Tamim` · `Turab` · `Mosambika`

---

## 👨‍💻 Author

**Md. Usha Khan**
Bangladesh University of Engineering & Technology (BUET)
Student ID: 2205065 | L-1 T-2 Project

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).
