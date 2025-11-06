package main;

import gui.ChessGUI;
import pieces.Piece;
import players.AiPlayer;
import players.HumanPlayer;
import players.Player;

public class Main {
    public static void main(String[] args) {
        util.Evaluation.initTables();

        javax.swing.SwingUtilities.invokeLater(() -> {
            Board board = new Board();

            HumanPlayer white = new HumanPlayer(0, Piece.WHITE);
            Player black = new AiPlayer(0, Piece.BLACK, /*difficulty/depth*/ 3);

            ChessGUI gui = new ChessGUI(board);
            GameController controller = new GameController(board, white, black, gui);

            controller.start();
        });
    }
}