package players;

import main.Board;
import util.Move;

import java.util.List;

public class AiPlayer extends Player {
    int difficulty;
    int turn = color;

    public AiPlayer(int timeLeft, int color,int difficulty) {
        this.timeLeft = timeLeft;
        this.color = color;
        this.difficulty = difficulty;
    }

    public Move move(Board board){
        return search(board);
    }

    public Move search(Board board){
        Move bestMove = null;
        int bestValue = (color == 1) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        List<Move> availableMoves = board.getAvailableMoves(turn);

        for (Move move : availableMoves) {
            Board boardCopy = new Board(board);
            boardCopy.makeMove(move);

            int eval = minimax(boardCopy, Integer.MIN_VALUE, Integer.MAX_VALUE, difficulty, 0);

            if (eval > bestValue) {
                bestValue = eval;
                bestMove = move;
            }
        }
        return bestMove;
    }

    public int minimax(Board board, int alpha, int beta, int depth, int maximazingPlayer){
      // if (depth == 0 || board.isGameOver(turn)) return staticPositionEval();

        List<Move> availableMoves = board.getAvailableMoves(turn);
        turn = 1 - turn;

        if (maximazingPlayer == 1) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : availableMoves) {
                Board boardCopy = new Board(board);
                boardCopy.makeMove(move);
                int eval = minimax(boardCopy, alpha, beta, depth - 1, 0);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }
            return maxEval;
        }
        else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : availableMoves) {
                Board boardCopy = new Board(board);
                boardCopy.makeMove(move);
                int eval = minimax(boardCopy, alpha, beta, depth - 1, 1);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }

}
