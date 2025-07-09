package pieces;

import static main.Board.*;

public class Pawn extends Piece {
    boolean wasMoved;

    public Pawn (int color,int row, int col) {
        super(color, row, col);
    }
// en passent

    @Override
    public boolean isMoveAllowed(int row, int col) {
        int deltaRow = row-getRow();
        int deltaCol = col-getCol();
        int color = getColor();

        if (deltaRow == 0 || !(deltaCol == 0)) return false;

        if (wasMoved && deltaRow > 1) return false;

        if (deltaRow == 1 && Math.abs(deltaCol) == 1){
            if (((this.getColor()) ^ (getPieceAtSquare(row,col).getColor())) == 0) return false;
        }

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
