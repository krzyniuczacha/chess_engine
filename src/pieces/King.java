package pieces;

import static main.Board.*;

public class King extends Piece {
    boolean wasMoved;

    public King(int color,int row, int col) {
        super(color, row, col);
        wasMoved = false;
    }

    @Override
    public boolean move(int row, int col) {
        if (!isMoveAllowed(row, col)) return false;
        setPosition(row,col);
        wasMoved = true;
        return true;
    }

    @Override
    public boolean isMoveAllowed(int row, int col) {
        int deltaRow = row-getRow();
        int deltaCol = col-getCol();

        if(isCheckableAfter(row, col)) return false;

        if ((deltaRow == 0 && (deltaCol == 3 || deltaCol == -4)) && !wasMoved) {
            if (!checkForCastling(this, row, col)) return false;
        }
        else if (Math.abs(deltaCol) > 1 || Math.abs(deltaRow) > 1) return false;

        if (deltaRow == 0 && deltaCol == 0) return false;

        if (row < 0 || row >7 || col < 0 || col >7) return false;

        if (isSquareTaken(row , col)){
            if (((this.getColor()) ^ (getPieceAtSquare(row,col).getColor())) == 0) return false;
        }

        return true;
    }
}
