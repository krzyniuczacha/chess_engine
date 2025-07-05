package pieces;


import static main.Board.*;

public class Pawn extends Piece {

    public Pawn (int color,int row, int col) {
        super(color, row, col);
    }

    @Override
    public boolean move(int row, int col) {
        if (!isMoveAllowed(row, col)) return false;

    }

    @Override
    public boolean isMoveAllowed(int row, int col) {
        int deltaRow = row-getRow();
        int deltaCol = col-getCol();
        int color = getColor();

        if (deltaRow == 0 || !(deltaCol == 0)) return false;

        if (row < 0 || row >7 || col < 0 || col >7) return false;

        if (color == WHITE) {
             if (deltaRow > 2) return false;
        }
        else {
            if (deltaRow < -2) return false;
        }

        for (int i = 1; i < deltaRow; i++) {
            if (isSquareTaken(getRow() + i, getCol())) return false;
        }

        return true;
    }
}
