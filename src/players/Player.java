package players;

import main.Board;
import util.Move;

public abstract class Player {
    public float timeLeft;
    public int color;

    public abstract Move move(Board board);

    public int getColor(){
        return color;
    }

    public float getTimeLeft(){
        return timeLeft;
    }

}
