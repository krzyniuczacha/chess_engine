package players;

import main.Board;
import util.Move;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class HumanPlayer extends Player {
    private final BlockingQueue<Move> inbox = new ArrayBlockingQueue<>(1);


    public HumanPlayer(int timeLeft, int color) {
        this.timeLeft = timeLeft;
        this.color = color;
    }

    public void postMove(Move move) {
        inbox.poll();
        inbox.offer(move);
    }

    @Override
    public Move move(Board board) {
        try {
            while (true) {
                Move m = inbox.take();
                if (m != null
                        && m.getPieceMoved() != null
                        && m.getPieceMoved().getColor() == color
                        && board.isMoveValid(m.getPieceMoved(), m.getEndRow(), m.getEndCol())) {
                    return m;
                }

            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
}

