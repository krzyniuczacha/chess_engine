package players;

import main.Board;
import util.Move;

public class HumanPlayer extends Player{

    public HumanPlayer(int timeLeft, int color) {
        this.timeLeft = timeLeft;
        this.color = color;
    }

    @Override
    public Move move(Board board) {
        return null;
    }
}
