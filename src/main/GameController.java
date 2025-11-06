package main;

import gui.ChessGUI;
import pieces.Piece;
import players.Player;
import players.HumanPlayer;
import util.Move;

import javax.swing.SwingUtilities;

public class GameController {

    private final Board board;
    private final Player white;
    private final Player black;
    private final ChessGUI gui;

    public GameController(Board board, Player white, Player black, ChessGUI gui) {
        this.board = board;
        this.white = white;
        this.black = black;
        this.gui = gui;
    }

    public void start() {
        gui.setOnHumanMove(move -> {
            new Thread(() -> {
                boolean applied = board.makeMove(move);

                if (!applied) return;

                gui.setLastMove(move);
                SwingUtilities.invokeLater(gui::repaintBoard);
                checkGameOver();

                Player currentPlayer = (board.colorToMove == Piece.WHITE) ? white : black;
                if (currentPlayer instanceof players.AiPlayer) {
                    System.out.println("AI is thinking...");
                    Move aiMove = currentPlayer.move(board);
                    if (aiMove != null && board.makeMove(aiMove)) {
                        System.out.println("AI made move: " + aiMove);
                        gui.setLastMove(aiMove);
                        SwingUtilities.invokeLater(gui::repaintBoard);
                        checkGameOver();
                    } else {
                        System.out.println("AI move failed or was null");
                    }
                }
            }, "move-processor").start();
        });

        if (board.colorToMove == white.getColor() && white instanceof players.AiPlayer) {
            new Thread(() -> {
                System.out.println("AI (White) is starting the game...");
                Move aiMove = white.move(board);
                if (aiMove != null && board.makeMove(aiMove)) {
                    gui.setLastMove(aiMove);
                    SwingUtilities.invokeLater(gui::repaintBoard);
                    checkGameOver();
                } else {
                    System.out.println("AI opening move failed");
                }
            }, "ai-move").start();
        }
    }

    private void checkGameOver() {
        if (board.isCheckmate(Piece.WHITE) || board.isCheckmate(Piece.BLACK) || board.isDraw()) {
            SwingUtilities.invokeLater(() -> gui.showGameOver(board));
        }
    }
}