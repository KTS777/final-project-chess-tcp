# ♟️ Chess Multiplayer (TCP/IP + PGN + Database)

This project is a full-featured two-player chess game built in Java using Swing for the GUI. It supports real-time multiplayer via TCP/IP,
records game data in a PostgreSQL database, and allows exporting completed games to `.pgn` format.

## 📦 Features

### ✅ Mandatory Features

* 🧩 **Combined two previous projects** into one fully functional application.
* 🖥️ **Client-Server Architecture** using TCP/IP socket communication.
* ♻️ **Two-player gameplay** with real-time move synchronization.
* 🗃️ **Game Recording**:

  * All played games are automatically saved into a PostgreSQL database.
* 📁 **PGN Exporting**:

  * At the end of a game, users can export the last played game as a `.pgn` file.

### 🛠 Technologies Used

* **Java (Swing)** – GUI and game logic
* **PostgreSQL** – Database to store game history
* **Java Sockets** – Networking between client and server
* **JDBC** – Database connection
* **Maven** – Dependency management

---

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/KTS777/final-project-chess-tcp.git
cd final-project-chess-tcp
```

### 2. Configure PostgreSQL

Create a PostgreSQL database named `chess_db` with the following schema:

```sql
CREATE TABLE games (
    id SERIAL PRIMARY KEY,
    white_player_id INT NOT NULL,
    black_player_id INT NOT NULL,
    winner VARCHAR(10) CHECK (winner IN ('1-0', '0-1', '1/2-1/2')) NOT NULL,
    pgn TEXT NOT NULL,
    played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Note:** Update the `DatabaseManager.java` file with your DB credentials.

---

## 🧪 How to Run

1. **Start the server**
   Run the `ChessServer` class to start accepting client connections.

2. **Start two clients**
   Run `ClientMain.java` twice.
   Each client will be assigned as White or Black.

3. **Play the game**
   Moves are synchronized between clients.

4. **End of game**
   When checkmate occurs, a dialog asks if you'd like to export the game.
   If "Yes", the PGN file will be saved to a selected directory.

---

## 📂 Project Structure

```
src/
└── main/java/
    ├── client/
    │   ├── controller/         # Game logic and timer
    │   ├── model/              # Clock
    │   ├── network/            # Client socket connection
    │   └── view/               # GUI components
    ├── database/               # Database and PGN exporting
    └── server/                 # Server connection handler
```

---

## 📌 Future Improvements

* ✅ Add user login and authentication
* ✅ Display player names in PGN export
* ✅ Enhance GUI with themes and animations
* ✅ Support for online matchmaking

---

## 🧑‍💻 Authors

* Partner 1 – [@GitHubProfile](https://github.com/KTS777)
* Partner 2 – [@GitHubProfile](https://github.com/temoqometiani)
* Project - [@GitHubProfile](https://github.com/KTS777/final-project-chess-tcp)

---

## 📃 License

This project is for educational purposes and is not intended for commercial use.


