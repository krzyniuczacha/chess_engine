package players;

import main.Board;
import util.Move;
import pieces.Piece;

import static util.Evaluation.staticPositionEval;

import java.util.List;

public class AiPlayer extends Player {
    int difficulty;

    public AiPlayer(int timeLeft, int color, int difficulty) {
        this.timeLeft = timeLeft;
        this.color = color;
        this.difficulty = difficulty;
    }

    public Move move(Board board){
        return search(board);
    }

    public Move search(Board board){
        Move bestMove = null;
        int bestValue = Integer.MAX_VALUE;
        List<Move> availableMoves = board.getAvailableMoves(this.color);

        int eval = 0;

        if (availableMoves.isEmpty()) {
            System.out.println("No moves available - chuj");
            return null;
        }

        for (Move move : availableMoves) {
            Board boardCopy = new Board(board);
            if (boardCopy.makeMove(move)) {
                eval = minimax(boardCopy, difficulty - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
                if (eval < bestValue) {
                    bestValue = eval;
                    bestMove = move;
                }
            }
        }
        System.out.println("Current evaluation: " + eval);
        return bestMove;
    }

    private int minimax(Board board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0) {
            int eval = staticPositionEval(board);
            //System.out.println("Terminal evaluation at depth 0: " + eval);
            return eval;
        }
        if (board.isGameOver(board.colorToMove)) {
            int eval = staticPositionEval(board);
            //System.out.println("Game over evaluation: " + eval);
            return eval;
        }
        List<Move> moves = board.getAvailableMoves(board.colorToMove);
        if (moves.isEmpty()) {
            int eval = staticPositionEval(board);
            //System.out.println("No moves available, evaluation: " + eval);
            return eval;
        }

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : moves) {
                Board copy = new Board(board);
                if (copy.makeMove(move)) {
                    int eval = minimax(copy, depth - 1, alpha, beta, false);
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : moves) {
                Board copy = new Board(board);
                if (copy.makeMove(move)) {
                    int eval = minimax(copy, depth - 1, alpha, beta, true);
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return minEval;
        }
    }
}