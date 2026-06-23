# Java Chess Engine

A fully playable chess application written in Java, featuring a drag-and-drop Swing GUI and an AI opponent powered by Minimax with alpha-beta pruning and PeSTO piece-square table evaluation.

---

## Features

- **Drag-and-drop GUI** built with Java Swing — click and drag pieces to make moves
- **Complete chess rules** — castling, en passant, pawn promotion, draw by repetition, and the 50-move rule
- **AI opponent** using Minimax search with alpha-beta pruning
- **PeSTO evaluation** — separate midgame and endgame piece-square tables with smooth game-phase interpolation
- **FEN support** — board state can be loaded from a FEN string
- **Human vs AI** or any player combination configurable in `Main.java`

---

## Requirements

- Java 11 or newer

---

## Building & Running

Compile all sources from the project root and run the main class:

```bash
javac -d out $(find src -name "*.java")
java -cp out main.Main
```

On Windows (PowerShell):

```powershell
Get-ChildItem -Recurse -Filter "*.java" src | ForEach-Object { $_.FullName } | Set-Content sources.txt
javac -d out @sources.txt
java -cp out main.Main
```

> **Piece images** must be placed in `res/pieces/` as PNG files. Expected naming convention: `white-queen.png`, `black-knight.png`, `white-king.png`, etc.

---

## Project Structure

```
src/
├── main/
│   ├── Main.java           # Entry point — configure players and difficulty here
│   ├── Board.java          # Board state, move validation, game-over detection
│   └── GameController.java # Turn loop, human/AI dispatch
├── pieces/
│   ├── Piece.java          # Abstract base class
│   ├── King.java
│   ├── Queen.java
│   ├── Rook.java
│   ├── Bishop.java
│   ├── Knight.java
│   └── Pawn.java
├── players/
│   ├── Player.java         # Abstract player interface
│   ├── HumanPlayer.java    # Receives moves from the GUI
│   └── AiPlayer.java       # Minimax search with alpha-beta pruning
├── gui/
│   └── ChessGUI.java       # Swing board rendering and user input
└── util/
    ├── Move.java           # Move representation
    ├── Square.java         # Board coordinate helper
    ├── FEN.java            # FEN string parser
    └── Evaluation.java     # PeSTO static evaluation function
```

---

## AI

The AI uses **Minimax with alpha-beta pruning**. The search depth controls the strength/speed trade-off and is configured in `Main.java`:

```java
Player black = new AiPlayer(0, Piece.BLACK, 3); // 3 = search depth
```

| Depth | Strength | Approximate think time |
|-------|----------|------------------------|
| 2     | Beginner | < 0.1 s                |
| 3     | Casual   | ~0.5 s                 |
| 4     | Medium   | ~3–10 s                |
| 5+    | Strong   | several seconds+       |

### Evaluation

Position scoring uses the **PeSTO** tables — widely used piece-square values with separate midgame (MG) and endgame (EG) weights per piece type. The final score is a weighted interpolation:

```
score = (mgScore × mgPhase + egScore × egPhase) / 24
```

where `gamePhase` is derived from the remaining material on the board (knights, bishops, rooks, and queens contribute to the phase counter).

---

## Configuring Players

Open `src/main/Main.java` to change the setup:

```java
HumanPlayer white = new HumanPlayer(0, Piece.WHITE);
Player black = new AiPlayer(0, Piece.BLACK, 3);  // change 3 to adjust difficulty
```

To play AI vs AI, replace both players with `AiPlayer` instances. To play human vs human, use two `HumanPlayer` instances.

---

## License

MIT
