# Java Chess

A two-player chess application written in Java, featuring a graphical interface and an AI opponent powered by the Minimax algorithm with alpha-beta pruning.

## Features

- Playable via drag-and-drop GUI
- Full chess rule support: castling, en passant, pawn promotion
- AI opponent with configurable difficulty (search depth)
- Position evaluation using PeSTO piece-square tables (midgame/endgame interpolation)

## Requirements

- Java 11+

## Running

Compile all sources from the `src/` directory and run `main.Main`:

```bash
javac -d out $(find src -name "*.java")
java -cp out main.Main
```

Piece images should be placed in `res/pieces/` as PNG files named `white-queen.png`, `black-knight.png`, etc.

## Project Structure

```
src/
├── main/       # Board logic, game controller, entry point
├── pieces/     # Piece classes (King, Queen, Rook, Bishop, Knight, Pawn)
├── players/    # HumanPlayer and AiPlayer
├── gui/        # Swing-based board rendering
└── util/       # Move, Square, FEN parser, evaluation
```

## AI

The AI uses Minimax with alpha-beta pruning. Difficulty is controlled by search depth, set in `Main.java`:

```java
Player black = new AiPlayer(0, Piece.BLACK, 3); // 3 = depth
```

Higher depth = stronger but slower play.
